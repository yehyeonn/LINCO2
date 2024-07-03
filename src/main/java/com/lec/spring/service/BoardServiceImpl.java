package com.lec.spring.service;

import com.lec.spring.domain.*;
import com.lec.spring.repository.*;
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

    private ClubRepository clubRepository;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    public BoardServiceImpl(SqlSession sqlSession){
        boardRepository = sqlSession.getMapper(BoardRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
        attachmentRepository = sqlSession.getMapper(AttachmentRepository.class);
        clubUserListRepository = sqlSession.getMapper(ClubUserListRepository.class);
        clubRepository = sqlSession.getMapper(ClubRepository.class);
    }

    @Override
    public int write(Board board, Map<String, MultipartFile> files) {
        User user = U.getLoggedUser();

        user = userRepository.findById(user.getId());
        board.setUser(user);
//        디버깅용
//        System.out.println("userID : " + user.getId());
//        System.out.println("Saving Board : " + board);

        // 클럽 정보 설정
        if(board.getBoardType() != null &&(board.getBoardType().getId() == 3)){
            Long clubId = board.getClub() != null ? board.getClub().getId() : null;
            if (clubId != null){
                Club club = clubRepository.findById(clubId);
                if (club != null){
                    board.setClub(club);
                }
            }
        } else if (board.getBoardType() != null && (board.getBoardType().getId() == 4 || board.getBoardType().getId() == 5)) {
            Long clubId = board.getClub().getId();

        }

        int cnt = boardRepository.save(board);

//        System.out.println("Saving cnt : " + cnt);

        addFiles(files, board.getId());

        return cnt;
    }

    private void addFiles(Map<String, MultipartFile> files, Long id) {
        if (files == null) return;

        for (Map.Entry<String, MultipartFile> e : files.entrySet()) {
            if (!e.getKey().startsWith("upfile"))
                continue;

//            System.out.println("\n첨부파일 정보 : " + e.getKey());
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

        String sourcename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String filename = sourcename;

        File file = new File(uploadDir, filename);
        if (file.exists()){
            int pos = filename.lastIndexOf(".");
            if (pos > -1){
                String name = filename.substring(0, pos);
                String ext = filename.substring(pos + 1);

                filename = name + "_" + System.currentTimeMillis() + "." + ext;
            }else {
                filename += "_" + System.currentTimeMillis();
            }
        }
//        디버깅용
//        System.out.println("filename : " + filename);

        Path copyOfLocation = Paths.get(new File(uploadDir, filename).getAbsolutePath());
//         경로 디버깅용
//        System.out.println("copyOfLocation : " + copyOfLocation);

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
                .filename(filename)
                .sourcename(sourcename)
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
        System.out.println("Board = " + board.toString());
        return board;
    }

    private void setImage(List<Attachment> fileList){
        String realPath = new File(uploadDir).getAbsolutePath();

        for (Attachment attachment : fileList){
            BufferedImage imgData = null;
            File f = new File(realPath, attachment.getFilename());
            try {
                imgData = ImageIO.read(f);
                if (imgData != null)
                    attachment.setImage(true);
            } catch (IOException e) {
                System.out.println("파일 존재 안 함 : " + f.getAbsolutePath() + "[" + e.getMessage() + "]");
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

            // 새로 추가
            for (Board board : list){
                List<Attachment> attachments = attachmentService.findByAttachment(board.getId());
                board.setFileList(attachments);
//                System.out.println("BOARD File List : " + board.getFileList());
                if (board.getFileList() != null && !board.getFileList().isEmpty()){
                    board.setImagePath("/upload/" + board.getFileList().get(0).getFilename());
                }else {
                    board.setImagePath("/upload/DefaultImg.jpg");
                }
            }

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
//        System.out.println("삭제시도 : " + f.getAbsolutePath());

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


    //클럽 게시판 리스트
    @Override
    public List<Board> clubPostList(Long id, Integer page ,Model model, String title) {

        // 현재페이지
        if (page == null || page < 1) page = 1; //디폴트 1page
        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지가 표시되나
        // pageRows: 한 '페이지'에 몇개의 글을 리스트 할것인가?
        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("writePages");
        if (writePages == null) writePages = WRITE_PAGES;
        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if (pageRows == null) pageRows = PAGE_ROWS;
        session.setAttribute("page", page); // 현재 페이지 번호 -> session 에 저장한다.

        long cnt = boardRepository.clubPostsListAll(id , title);   //글 목록 전체의 개수
        int totalPage = (int) Math.ceil(cnt / (double) pageRows);  // 총 몇 페이지인지 분량

        //[페이징] 에 표시할 '시작페이지' 와 '마지막페이지'
        int startPage = 0;
        int endPage = 0;

        // 해당 '페이지'의 글 목록
        List<Board> list = null;

        if (cnt > 0) {  // 데이터가 최소 1개 이상 있는 경우만 페이징
            // page 값 보정
            if (page > totalPage) {
                page = totalPage;
            }

            // fromRow : 몇번째 데이터부터
            int fromRow = (page - 1) * pageRows;

            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            // 해당 페이지의 글 목록 읽어오기
            list = boardRepository.findbyClubPosts(id, fromRow, pageRows, title);
            model.addAttribute("posts", list);
            model.addAttribute("title",title);
        } else {
            page = 0;
        }

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
}
