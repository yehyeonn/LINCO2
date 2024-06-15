package com.lec.spring.service;

import com.lec.spring.domain.User;
import com.lec.spring.domain.UserAuthority;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    boolean isExist(String username);
    int register(User user);
    List<UserAuthority> selectAuthorityById(Long id);

}
