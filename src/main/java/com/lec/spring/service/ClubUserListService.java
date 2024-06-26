package com.lec.spring.service;

import com.lec.spring.domain.ClubUserList;

import java.util.List;

public interface ClubUserListService {
    List<ClubUserList> addUserToClub(Long userId, Long clubId);

    List<ClubUserList> findByClubId(Long clubId);

    List<ClubUserList> findByUserId(Long userId);
}
