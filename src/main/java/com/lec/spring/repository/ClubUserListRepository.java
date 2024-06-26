package com.lec.spring.repository;

import com.lec.spring.domain.ClubUserList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClubUserListRepository {

    // 새로운 클럽 사용자 추가
    int save(ClubUserList clubUserList);

    // 특정 클럽의 사용자 목록 조회
    List<ClubUserList> findByClubId(@Param("club_id") Long club_id);

    // 특정 사용자가 가입한 클럽 목록 조회
    List<ClubUserList> findByUserId(Long user_id);

    // 특정 클럽의 마스터 조회
    ClubUserList findClubMaster(@Param("club_id") Long club_id); // TODO 클럽 아이디로 클럽장 조회

    // 특정 클럽의 특정 사용자 조회
    ClubUserList findByUserIdAndClubId(@Param("user_id") Long user_id, @Param("club_id") Long club_id);


    // 클럽의 멤버 삭제하기
    int deleteByUserId(@Param("club_id") Long club_id);

    // 클럽 회원들의 인원수 조회
    int getClubMemberCount(@Param("club_id") Long club_id);

}

