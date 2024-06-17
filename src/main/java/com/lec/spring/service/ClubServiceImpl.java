package com.lec.spring.service;

import com.lec.spring.domain.Club;
import com.lec.spring.domain.ClubUserList;
import com.lec.spring.repository.ClubRepository;
import com.lec.spring.repository.ClubUserListRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final ClubUserListRepository clubUserListRepository;

    @Autowired
    public ClubServiceImpl(SqlSession sqlSession) {
        this.clubRepository = sqlSession.getMapper(ClubRepository.class);
        this.clubUserListRepository = sqlSession.getMapper(ClubUserListRepository.class);
    }

    @Override
    @Transactional
    // 클럽을 생성하고, 클럽 생성자를 'MASTER' 역할로 ClubUserList에 추가
    public int createClub(Club club, Long userId) {
        int result = clubRepository.save(club);
        if (result > 0) {
            ClubUserList clubUserList = new ClubUserList();
            clubUserList.setUserId(userId);
            clubUserList.setClubId(club.getId());
            clubUserList.setRole("MASTER");
            clubUserListRepository.save(clubUserList);
        }
        return result;
    }

    @Override
    // 특정 id 클럽의 상세 내용 수정(소개, 상세내용, 대표 이미지)
    public int updateClub(Club club) {
        return clubRepository.update(club);
    }

    @Override
    // 전체 클럽 목록을 조회
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    @Override
    // 특정 클럽을 조회
    public Club getClubById(Long clubId) {
        return clubRepository.findById(clubId);
    }

    @Override
    //특정 클럽의 사용자 목록을 조회
    public List<ClubUserList> getClubUsers(Long clubId) {
        return clubUserListRepository.findByClubId(clubId);
    }

    @Override
    // 특정 사용자가 가입한 클럽 목록을 조회
    public List<ClubUserList> getUserClubs(Long userId) {
        return clubUserListRepository.findByUserId(userId);
    }


    @Override
    @Transactional
    public int deleteClubUser(Long userId, Long clubId, Long requestingUserId) throws IllegalAccessException {
        // requestingUserId가 MASTER 인지 확인하기
        ClubUserList requester = clubUserListRepository.findByUserIdAndClubId(requestingUserId, clubId);
        if (requester == null || !"MASTER".equals(requester.getRole())) {
            throw new IllegalAccessException("You do not have permission to perform this action.");
        }

        return clubUserListRepository.deleteByUserIdAndClubId(userId, clubId);
    }
}
