package com.lec.spring.repository;

import com.lec.spring.domain.User;

public interface UserRepository {
    User findById(Long id);
    User findByUsername(String username); // 이메일
    int save(User user);
    int update(User user);

//    // id로 username 찾기
//    Long findUsername(Long id);


}
