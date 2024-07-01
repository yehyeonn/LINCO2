package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Club;
import com.lec.spring.domain.ClubUserList;
import com.lec.spring.domain.User;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@Service
public class ClubServiceImpl implements ClubService {


    // 첨부파일 업로드
    @Value("upload")
    private String uploadDir;

    @Value("5")
    private int WRITE_PAGES;

    @Value("15")
    private int PAGE_ROWS;

    private final ClubRepository clubRepository;
    private final ClubUserListRepository clubUserListRepository;
    private final UserRepository userRepository;
    private AttachmentRepository attachmentRepository;

    @Autowired
    public ClubServiceImpl(SqlSession sqlSession) {
        this.clubRepository = sqlSession.getMapper(ClubRepository.class);
        this.clubUserListRepository = sqlSession.getMapper(ClubUserListRepository.class);
        this.userRepository = sqlSession.getMapper(UserRepository.class);
        this.attachmentRepository = sqlSession.getMapper(AttachmentRepository.class);
    }

    @Override
    public int deleteById(Long club_id) {
        int result = clubRepository.deleteById(club_id);
        return result;
    }

    @Override
    public boolean isClubNameExists(String clubName) {
        // 클럽 이름이 이미 존재하는지 확인하는 로직
        Club club = clubRepository.findByName(clubName);
        return club != null; // 클럽 객체가 null이 아니면 이미 존재하는 것으로 간주
    }

    @Override
    public List<Attachment> findByClubId(Long club_id) {
        return attachmentRepository.findByClubId(club_id);
    }

    @Override
    public int uploadImg(Long club_id, MultipartFile file) {
        User user = U.getLoggedUser();

        int cnt = addFiles(file, club_id);

        return cnt;
    }


    private int addFiles(MultipartFile files, Long club_id) {
        if (files == null) return 0;

        Attachment file = upload(files);

        if (file != null) {
            file.setClub_id(club_id);
            attachmentRepository.save(file);
        }

        return 1;
    }

    private Attachment upload(MultipartFile multipartFile) {
        Attachment attachment = null;

        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) return null;

        String sourceName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String fileName = sourceName;

        System.out.println(sourceName);

        File file = new File(uploadDir, fileName);
        if (file.exists()) {
            int pos = fileName.lastIndexOf(".");
            if (pos > -1) {    // 확장자가 있는경우
                String name = fileName.substring(0, pos);
                String ext = fileName.substring(pos + 1);

                fileName = name + "_" + System.currentTimeMillis() + "." + ext;
            } else {
                fileName += "_" + System.currentTimeMillis();
            }
        }
        System.out.println(fileName);

        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());
        System.out.println(copyOfLocation);

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
    // 클럽 생성 (유저가 master 가 됨)
    public int createClub(Club club) {
        // 현재 로그인 한 작성자 정보
        User user = U.getLoggedUser();

        // 위 정보는 session 의 정보이고, 일단 DB 에서 다시 읽어온다.
        user = userRepository.findById(user.getId());
        System.out.println("유저의 id는?? " + user.getId());
        int result = clubRepository.save(club);
        System.out.println("클럽의 id는?? " + club.getId());
        if (result > 0) {
            ClubUserList clubUserList = new ClubUserList();
            clubUserList.setUser_id(user.getId());
            clubUserList.setClub_id(club.getId());
            clubUserList.setRole("MASTER");
            clubUserListRepository.save(clubUserList);
        }
        return result;
    }


    @Override
    @Transactional
    // 클럽 가입 (유저가 member 가 됨)
    public int addMemberToClub(Long user_id, Long club_id) {
        ClubUserList clubUserList = new ClubUserList();
        clubUserList.setUser_id(user_id);
        clubUserList.setClub_id(club_id);
        clubUserList.setRole("MEMBER");
        int result = clubUserListRepository.save(clubUserList);
        return result;
    }

    @Override
    @Transactional
    public void deleteClubMember(Long club_id, Long user_id) {
        clubUserListRepository.findByUserId(user_id);

    }


    @Override
    public int updateClub(Club club) {
        return clubRepository.update(club);
    }

    @Override
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    @Override
    public List<Club> list(Integer page, Model model, String selectcategory, String selectdetailcategory) {
        System.out.println("selectcategory:" + selectcategory);
        System.out.println("selectdetailcategory:" + selectdetailcategory);

        if (page == null || page < 1) page = 1;

        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("writePages");
        if (writePages == null) writePages = WRITE_PAGES;

        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if (pageRows == null) pageRows = PAGE_ROWS;
        session.setAttribute("page", page);

        long cnt = clubRepository.countSelect(selectcategory, selectdetailcategory);
        int totalPage = (int) Math.ceil(cnt / (double) pageRows);

        int startPage = 0;
        int endPage = 0;

        List<Club> list = null;

        if (cnt > 0) {
            if (page > totalPage) page = totalPage;
            int fromRow = (page - 1) * pageRows;

            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            list = clubRepository.selectFromRow(fromRow, pageRows, selectcategory, selectdetailcategory);

            model.addAttribute("list", list);
            model.addAttribute("category", selectcategory);
            model.addAttribute("detailcategory", selectdetailcategory);
        } else {
            page = 0;
        }
        model.addAttribute("cnt", cnt);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageRows", pageRows);

        model.addAttribute("url", U.getRequest().getRequestURI());      // 목록의 url
        model.addAttribute("writePages", writePages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return list;
    }


    @Override
    public ClubUserList findClubMaster(Long club_id) {
        return clubUserListRepository.findClubMaster(club_id);
    }


    @Override
    public Club getClubById(Long club_id) {
        return clubRepository.findById(club_id);
    }


    @Override
    public int getClubMemberCount(Long club_id) {
        return clubUserListRepository.getClubMemberCount(club_id);
    }
}
