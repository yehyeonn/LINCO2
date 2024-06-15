package com.lec.spring.service;

import com.lec.spring.domain.Socializing;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SocializingService {
    // 작성, 내용보기, 전체 목록, 수정하기, 삭제
    int write(Socializing socializing);

    @Transactional
    Socializing detail(Long id);

    List<Socializing> list();

    // 수정
    Socializing selectById(Long id);
    // 제목, 내용
    int update(Socializing socializing);

    // 삭제
    int deleteById(Long id);
}
