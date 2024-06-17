package com.lec.spring.service;

import com.lec.spring.domain.Club;
import com.lec.spring.domain.ClubUserList;

import java.util.List;

public interface ClubService {

    // 클럽 생성
    int createClub(Club club, Long userId);

    // 클럽 정보 수정
    int updateClub(Club club);

    // 클럽 목록 조회
    List<Club> getAllClubs();

    // 특정 클럽 조회
    Club getClubById(Long clubId);

    // 클럽 사용자 목록 조회
    List<ClubUserList> getClubUsers(Long clubId);

    // 특정 사용자의 클럽 목록 조회
    List<ClubUserList> getUserClubs(Long userId);


    // 클럽 사용자 삭제 (MASTER만 가능)
    int deleteClubUser(Long userId, Long clubId, Long requestingUserId) throws IllegalAccessException;
}
