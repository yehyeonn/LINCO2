package com.lec.spring.service;

import com.lec.spring.domain.ClubUserList;
import com.lec.spring.repository.ClubUserListRepository;
import com.lec.spring.repository.UserRepository;
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
    public List<ClubUserList> findByClubId(Long clubId) {
        return clubUserListRepository.findByClubId(clubId);
    }

    @Override
    public List<ClubUserList> findByUserId(Long userId) {
        return clubUserListRepository.findByUserId(userId);
    }
}
