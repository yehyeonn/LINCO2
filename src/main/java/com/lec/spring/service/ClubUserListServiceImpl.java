package com.lec.spring.service;

import com.lec.spring.domain.ClubUserList;
import com.lec.spring.domain.User;
import com.lec.spring.repository.ClubUserListRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.U;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubUserListServiceImpl implements ClubUserListService {

    private UserRepository userRepository;
    private ClubUserListRepository clubUserListRepository;

    @Autowired
    public ClubUserListServiceImpl(SqlSession sqlSession){
        userRepository = sqlSession.getMapper(UserRepository.class);
        clubUserListRepository = sqlSession.getMapper(ClubUserListRepository.class);
    }

    @Override
    public List<ClubUserList> addUserToClub(Long userId, Long clubId) {
        List<ClubUserList> result = null;
        result = clubUserListRepository.findByClubuserList(userId, clubId);
        return result;
    }

    @Override
    public int deleteByClubIdAndUserId(Long user_id, Long club_id) {
        int result = clubUserListRepository.deleteByClubIdAndUserId(user_id, club_id);
        return result;
    }

    @Override
    public List<ClubUserList> findByClubId(Long clubId) {
        return clubUserListRepository.findByClubId(clubId);
    }

    @Override
    public List<ClubUserList> findByUserId(Long userId) {
//        User user = U.getLoggedUser();

//        System.out.println("userId : " + userId);
//        user = userRepository.findById(userId);
//        System.out.println("USERID : " + user.getId());

        return clubUserListRepository.findByUserId(userId);
    }

    @Override
    public List<ClubUserList> clubuserlist(Long id) {
        return clubUserListRepository.clubuserlist(id);
    }
}
