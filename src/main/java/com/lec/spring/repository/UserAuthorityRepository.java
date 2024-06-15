package com.lec.spring.repository;

import com.lec.spring.domain.User;
import com.lec.spring.domain.UserAuthority;

import java.util.List;

public interface UserAuthorityRepository {

    UserAuthority findByName(String name);
    List<UserAuthority> findByUser(User user);
    int addUserAuthority(Long userId, Long userAuthorityId);
}
