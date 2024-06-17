package com.lec.spring.repository;

import com.lec.spring.domain.Socializing;

import java.util.List;

public interface SocializingRepository {
    int save(Socializing socializing);
    Socializing findById(Long id);
    List<Socializing> findAll();
    int update(Socializing socializing);
    int delete(Socializing socializing);
}
