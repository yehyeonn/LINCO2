package com.lec.spring.repository;

import com.lec.spring.domain.Reservation;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    int save(Reservation reservation);


    Reservation findById(Long id);

    int update(Reservation reservation);

    List<Reservation> findByVenueAndDate(Long venue_id, String reserve_date);

    List<Reservation> findPayedReservation();

    List<Reservation> findByUserId(Long userId);

    Reservation findByMerchant(String MerchantId);

}
