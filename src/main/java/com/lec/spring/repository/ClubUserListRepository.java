package com.lec.spring.repository;

import com.lec.spring.domain.ClubUserList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClubUserListRepository {

    // 새로운 클럽 사용자 추가
    int save(ClubUserList clubUserList);

    // 특정 클럽의 사용자 목록 조회
    List<ClubUserList> findByClubId(Long clubId);

    // 특정 사용자가 가입한 클럽 목록 조회
    List<ClubUserList> findByUserId(Long userId);

    // 특정 클럽의 특정 사용자 조회
    ClubUserList findByUserIdAndClubId(@Param("userId") Long userId, @Param("clubId") Long clubId);



    // 특정 클럽의 특정 사용자 삭제
    int deleteByUserIdAndClubId(@Param("userId") Long userId, @Param("clubId") Long clubId);
}

