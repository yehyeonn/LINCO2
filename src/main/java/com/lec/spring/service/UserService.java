package com.lec.spring.service;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.ClubUserList;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserSocializing;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User findByUserId(Long userId);
    User findById(Long id);
    boolean isExist(String username);
    int register(User user);
    List<Authority> selectAuthoritiesById(Long id);

    List<ClubUserList> getUserClubs(Long user_id);
    List<UserSocializing> getUserSocializings(Long user_id);
}
