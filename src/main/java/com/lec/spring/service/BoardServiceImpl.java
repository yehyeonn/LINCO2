package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Board;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AttachmentRepository;
import com.lec.spring.repository.BoardRepository;
import com.lec.spring.repository.ClubUserListRepository;
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

    // 페이징 갯수
    private int WRITE_PAGES=5;

    // 페이징 안의 페이지 갯수
    private int PAGE_ROWS=10;

    // 글 작성 관련 정보 가져오기
    private BoardRepository boardRepository;

    // 로그인한 정보 가져오기
    private UserRepository userRepository;

    // 권한설정 정보 가져오기
    private AttachmentRepository attachmentRepository;

    private ClubUserListRepository clubUserListRepository;

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

        // 디버깅용
        System.out.println("Saving Board : " + board);

        int cnt = boardRepository.save(board);

        System.out.println("Saving cnt : " + cnt);

        addFiles(files, board.getId());

        return cnt;
    }

    private void addFiles(Map<String, MultipartFile> files, Long id) {
        if (files == null) return;

        for (Map.Entry<String, MultipartFile> e : files.entrySet()) {
            if (!e.getKey().startsWith("upfile"))
                continue;

            System.out.println("\n첨부파일 정보 : " + e.getKey());
            U.printFileInfo(e.getValue());
            Attachment file = upload(e.getValue());

            if (file != null){
                file.setBoard_id(id);
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
        // 디버깅용
        System.out.println("fileName : " + fileName);

        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());
        // 경로 디버깅용
        System.out.println("copyOfLocation : " + copyOfLocation);

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
                .filename(fileName)
                .sourcename(sourceName)
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
            File f = new File(realPath, attachment.getFilename());
            try {
                BufferedImage imgData = ImageIO.read(f);
                if (imgData != null) attachment.setImage(true);
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
    public List<Board> list(Integer page, Model model,Long boardTypeId, Long clubId) {
//        System.out.println("boardId:" + boardTypeId);
//        System.out.println("clubId: " + clubId);
        if (page == null || page < 1) page = 1;

        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("writePages");
        if (writePages == null) writePages = WRITE_PAGES;

        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if (pageRows == null) pageRows = PAGE_ROWS;

        session.setAttribute("page", page);

        long cnt = boardRepository.countAll(boardTypeId, clubId);
//        System.out.println("board 개수 : "+cnt);
        int totalPage = (int) Math.ceil(cnt / (double) pageRows);

        int startPage = 0;
        int endPage = 0;

        List<Board> list = null;

        if (cnt > 0) {
            if (page > totalPage) page = totalPage;
            int fromRow = (page - 1) * pageRows;

            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            list = boardRepository.selectFromRow(fromRow, pageRows,boardTypeId, clubId);

//            디버깅용
//            for (int i = 0; i < list.size(); i++) {
//                System.out.println(list.get(i).toString()+"\n");
//            }
//            System.out.println("list 개수"+list.size());
            model.addAttribute("list", list);
            model.addAttribute("boardTypeId", boardTypeId);
            model.addAttribute("clubId", clubId);
        } else {
            page = 0;
        }

        model.addAttribute("cnt", cnt);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageRows", pageRows);

        model.addAttribute("url", U.getRequest().getRequestURI());
        model.addAttribute("writePages", writePages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return list;
    }


    @Override
    public Board findById(Long id) {
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
        return boardRepository.findByBoardTypeId(boardTypeId);
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
        File f = new File(saveDirectory, file.getFilename());
        System.out.println("삭제시도 : " + f.getAbsolutePath());

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
        Board board = boardRepository.findById(id);
        if (board != null) {
            List<Attachment> fileList = attachmentRepository.findByBoard(id);

            for (Attachment file : fileList){
                delFile(file);
            }
            return boardRepository.delete(board);
        }
        return 0;
    }
}
