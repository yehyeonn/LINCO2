package com.lec.spring.repository;

import com.lec.spring.domain.Socializing;

import java.util.List;

public interface SocializingRepository {
    int save(Socializing socializing);
    Socializing findById(Long id);
    List<Socializing> findAll();
    int update(Socializing socializing);
    int delete(Socializing socializing);

    List<Socializing> selectFromRow(int from, int rows);

    // 전체 글의 개수 가져오기
    int countAll();
}
