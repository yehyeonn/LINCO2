package com.lec.spring.service;

import com.lec.spring.domain.Reservation;

import java.util.List;

public interface ReservationService {
    int write(Reservation reservation);

    List<Reservation> list();

    int update(Reservation reservation);
}
