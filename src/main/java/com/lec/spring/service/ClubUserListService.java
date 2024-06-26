package com.lec.spring.service;

import com.lec.spring.domain.ClubUserList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClubUserListService {
    List<ClubUserList> addUserToClub(Long userId, Long clubId);

    List<ClubUserList> findByClubId(Long clubId);

    List<ClubUserList> findByUserId(@Param("user_id") Long userId);
}
