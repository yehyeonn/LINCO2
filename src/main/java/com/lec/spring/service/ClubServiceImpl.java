package com.lec.spring.service;

import com.lec.spring.domain.Club;
import com.lec.spring.domain.ClubUserList;
import com.lec.spring.domain.User;
import com.lec.spring.repository.ClubRepository;
import com.lec.spring.repository.ClubUserListRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.U;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final ClubUserListRepository clubUserListRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClubServiceImpl(SqlSession sqlSession) {
        this.clubRepository = sqlSession.getMapper(ClubRepository.class);
        this.clubUserListRepository = sqlSession.getMapper(ClubUserListRepository.class);
        this.userRepository = sqlSession.getMapper(UserRepository.class);
    }

    @Override
    @Transactional
    // 클럽 생성 (유저가 master 가 됨)
    public int createClub(Club club) {
        // 현재 로그인 한 작성자 정보
//        User user = U.getLoggedUser();

        // 위 정보는 session 의 정보이고, 일단 DB 에서 다시 읽어온다.
//        user = userRepository.findById(user.getId());


        int result = clubRepository.save(club);
//        if (result > 0) {
//            ClubUserList clubUserList = new ClubUserList();
////            clubUserList.setUser_id(user.getId()); // TODO 유저 id 직접 집어 넣어야함
//            clubUserList.setUser_id(1L);
//            clubUserList.setClub_id(club.getId());
//            clubUserList.setRole("MASTER");
//            clubUserListRepository.save(clubUserList);
//        }
        return result;
    }


    @Override
    @Transactional
    // 클럽 가입 (유저가 member 가 됨)
    public void addMemberToClub(Long user_id, Long club_id) {
        ClubUserList clubUserList = new ClubUserList();
        clubUserList.setUser_id(user_id);
        clubUserList.setClub_id(club_id);
        clubUserList.setRole("MEMBER");
        clubUserListRepository.save(clubUserList);
    }

    @Override
    @Transactional
    public void deleteClubMember(Long club_id, Long user_id) {
        List<ClubUserList> clubUserList = clubUserListRepository.findByClubId(club_id);
        ClubUserList masterUser = null;
        for (ClubUserList user : clubUserList) {
            if (user.getRole().equals("MASTER")) {
                masterUser = user;
                break;
            }
        }

//        if (masterUser != null && masterUser.getUser_id() == user_id) {
//            clubUserListRepository.deleteByUserId(club_id));
//        }
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
    public Club getClubById(Long club_id) {
        return clubRepository.findById(club_id);
    }

    @Override
    public List<ClubUserList> getClubUsers(Long club_id) {
        return clubUserListRepository.findByClubId(club_id);
    }

    @Override
    public List<ClubUserList> getUserClubs(Long user_id) {
        return clubUserListRepository.findByUserId(user_id);
    }
}
