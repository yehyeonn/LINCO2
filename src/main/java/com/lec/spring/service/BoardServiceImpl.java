package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Board;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AttachmentRepository;
import com.lec.spring.repository.BoardRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService{

    // 첨부파일 업로드
    @Value("upload")
    private String uploadDir;

//     페이징 갯수
    @Value("5")
    private int WRITE_PAGES;

//     페이징 안의 페이지 갯수
    @Value("6")
    private int PAGE_ROWS;

    // 글 작성 관련 정보 가져오기
    private BoardRepository boardRepository;

    // 로그인한 정보 가져오기
    private UserRepository userRepository;

    // 권한설정 정보 가져오기
    private AttachmentRepository attachmentRepository;

    @Autowired
    public BoardServiceImpl(SqlSession sqlSession){
        boardRepository = sqlSession.getMapper(BoardRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
        attachmentRepository = sqlSession.getMapper(AttachmentRepository.class);
    }

    @Override
    public int write(Board board, Map<String, MultipartFile> files) {
        User user = U.getLoggedUser();

        user = userRepository.findById(user.getId());
        board.setUser(user);

        int cnt = boardRepository.save(board);

        addFiles(files, board.getId());

        return cnt;
    }

    private void addFiles(Map<String, MultipartFile> files, Long id) {
        if (files == null) return;

        for (Map.Entry<String, MultipartFile> e : files.entrySet()) {
            if (!e.getKey().startsWith("upfile"))
                continue;

            Attachment file = upload(e.getValue());

            if (file != null){
                Board board = new Board();
                board.setId(id);
                file.setBoard(board);

                attachmentRepository.save(file);
            }
        }
    }

    private Attachment upload(MultipartFile multipartFile){
        Attachment attachment = null;

        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) return null;

        String sourceName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        String fileName = sourceName;

        File file = new File(uploadDir, fileName);
        if (file.exists()){
            int pos = fileName.lastIndexOf(".");
            if (pos > -1){
                String name = fileName.substring(0, pos);
                String ext = fileName.substring(pos + 1);

                fileName = name + "_" + System.currentTimeMillis() + "." + ext;
            }else {
                fileName += "_" + System.currentTimeMillis();
            }
        }
        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());
        try {
            Files.copy(
                multipartFile.getInputStream(),
                copyOfLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        attachment = Attachment.builder()
                .fileName(fileName)
                .sourceName(sourceName)
                .build();

        return attachment;
    }

    @Override
    @Transactional
    public Board detail(Long id) {
        boardRepository.incViewCnt(id);
        Board board = boardRepository.findById(id);

        if (board != null){
            List<Attachment> fileList = attachmentRepository.findByBoard(board.getId());

            setImage(fileList);

            board.setFileList(fileList);
        }

        return board;
    }

    private void setImage(List<Attachment> fileList){
        String realPath = new File(uploadDir).getAbsolutePath();

        for (Attachment attachment : fileList){
            BufferedImage imgData = null;
            File f = new File(realPath, attachment.getFileName());

            try {
                imgData = ImageIO.read(f);

                if (imgData != null)
                    attachment.setImage(true);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Board> list() {
        return boardRepository.findAll();
    }

    // 페이지 네이션 관련
    @Override
    public List<Board> list(Integer page, Model model) {
        if (page == null || page < 1) page = 1;

        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("writePages");
        if (writePages == null) writePages = WRITE_PAGES;

        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if (pageRows == null) pageRows = PAGE_ROWS;

        session.setAttribute("page", page);

        long cnt = boardRepository.countAll();
        int totalPage = (int) Math.ceil(cnt / (double) pageRows);

        // 하단에 표현될 [페이징] 의 '시작페이지'와 '마지막페이지'
        int startPage = 0;
        int endPage = 0;

        // 해당 페이지의 글 목록을 리스트로 준비
        List<Board> list = null;

        if (cnt > 0) {       // 데이터가 최소 1개 이상인 경우만 페이징

            // page 값 보정 (존재하지 않는 페이지에 대한 설정)
            if (page > totalPage) page = totalPage;

            // 쿼리문에 보내줄 fromRow 계산  (몇번째 데이터부터 select 할지)
            int fromRow = (page - 1) * pageRows;

            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            // 해당 페이지의 글 목록을 DB 에서 읽어오기
            list = boardRepository.selectFromRow(fromRow, pageRows);
            model.addAttribute("list", list);       // model 에 페이지의 글 목록 담기
        } else {
            page = 0;       // 글 목록이 없을 경우 페이징은 0으로 세팅
        }
        // 현재 선언한 페이지들의 템플릿도 가져와야 한다. (startPage, endPage, page ...)
        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수

        // [페이징]
        model.addAttribute("url", U.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지

        return list;
    }

    @Override
    public Board selectById(Long id) {
        Board board = boardRepository.findById(id);

        if (board != null){
            List<Attachment> fileList = attachmentRepository.findByBoard(board.getId());
            setImage(fileList);
            board.setFileList(fileList);
        }
        return board;
    }

    @Override
    public List<Board> findByBoardTypeId(Long boardTypeId) {
        return (List<Board>) boardRepository.findByBoardTypeId(boardTypeId);
    }

    @Override
    public int update(Board board, Map<String, MultipartFile> files, Long[] delfile) {
        int result = 0;

        result = boardRepository.update(board);

        addFiles(files, board.getId());

        if (delfile != null){       // 삭제할 첨부파일이 있을 경우
            for (Long fileId : delfile) {
                Attachment file = attachmentRepository.findById(fileId);
                if (file != null){
                    delFile(file);      // 물리적으로 파일 삭제
                    attachmentRepository.delete(file);      // DB 에서 삭제
                }
            }
        }

        return result;
    }

    private void delFile(Attachment file) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();       // 파일의 저장 경로를 절대경로로 가져오기

        File f = new File(saveDirectory, file.getFileName());

        if (f.exists()){
            if (f.delete()){
                System.out.println("삭제 성공");
            }else {
                System.out.println("삭제 실패");
            }
        }else {
            System.out.println("파일이 존재하지 않음");
        }
    }

    @Override
    public int deleteById(Long id) {
        int result = 0;

        Board board = boardRepository.findById(id);
        if (board != null){
            List<Attachment> fileList = attachmentRepository.findByBoard(id);
            if (fileList != null || fileList.size() > 0){
                for (Attachment file : fileList){
                    delFile(file);
                }
            }
            result = boardRepository.delete(board);
        }
        return result;
    }
}
