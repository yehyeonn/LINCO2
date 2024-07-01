package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Club;
import com.lec.spring.domain.ClubUserList;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AttachmentRepository;
import com.lec.spring.repository.ClubRepository;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
    private final AttachmentRepository attachmentRepository;

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

    private final UserRepository userRepository;

    @Autowired
    public ClubServiceImpl(SqlSession sqlSession) {
        this.clubRepository = sqlSession.getMapper(ClubRepository.class);
        this.clubUserListRepository = sqlSession.getMapper(ClubUserListRepository.class);
        this.userRepository = sqlSession.getMapper(UserRepository.class);
        this.attachmentRepository = sqlSession.getMapper(AttachmentRepository.class);
    }

    @Override
    @Transactional
    // 클럽 생성 (유저가 master 가 됨)
    public int createClub(Club club) {
        // 현재 로그인 한 작성자 정보
        User user = U.getLoggedUser();

        // 위 정보는 session 의 정보이고, 일단 DB 에서 다시 읽어온다.
        user = userRepository.findById(user.getId());
        System.out.println("유저의 id는?? "+ user.getId());
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
    @Transactional
    public Club detail(Long id) {
        Club club = clubRepository.findById(id);

        if (club != null){
            List<Attachment> fileList = attachmentRepository.findByClub(club.getId());
            setImage(fileList);
            club.setFileList(fileList);
        }
        System.out.println("club 게시판 상세페이지 : " + club.toString());
        return club;
    }

    private void setImage(List<Attachment> fileList) {
        String realPath = new File(uploadDir).getAbsolutePath();

        for (Attachment attachment : fileList){
            BufferedImage imgData = null;
            File f = new File(realPath, attachment.getFilename());
            try {
                imgData = ImageIO.read(f);
                if (imgData != null) attachment.setImage(true);
            } catch (IOException e) {
                System.out.println("클럽 게시판에 파일 존재 안 함 : " + f.getAbsolutePath() + "[" + e.getMessage() + "]");
            }
        }
    }

    @Override
    public ClubUserList findClubMaster(Long club_id){
        return clubUserListRepository.findClubMaster(club_id);
    }


    @Override
    public Club getClubById(Long club_id) {
        return clubRepository.findById(club_id);
    }





    @Override
    public int getClubMemberCount(Long club_id){
        return clubUserListRepository.getClubMemberCount(club_id);
    }
}
