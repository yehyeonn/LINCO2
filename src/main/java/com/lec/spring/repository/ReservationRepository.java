package com.lec.spring.repository;

import com.lec.spring.domain.Reservation;

import java.util.List;

public interface ReservationRepository {

    int save(Reservation reservation);


    List<Reservation> findById(Long id);

    int update(Reservation reservation);

    List<Reservation> findByVenueAndDate(Long venue_id, String reserve_date);

    List<Reservation> findPayedReservation();

}
