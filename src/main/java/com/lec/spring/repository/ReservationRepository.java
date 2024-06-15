package com.lec.spring.repository;

import com.lec.spring.domain.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();
    List<Reservation> findByUserid(Long id);
}
