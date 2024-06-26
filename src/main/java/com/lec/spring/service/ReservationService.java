package com.lec.spring.service;

import com.lec.spring.domain.Reservation;

import java.io.IOException;
import java.util.List;

public interface ReservationService {
    int write(Reservation reservation);

    List<Reservation> list();

    // 결제 취소 시 사용
    int update(Reservation reservation);

    List<Reservation> findByVenueAndDate(Long id, String reserve_date);

    List<Reservation> findPayedReservation();

    List<Reservation> findByUserId(Long user_id);

    void updateExpiredReservationStatus();

    Reservation findById(Long id);

    Reservation findByImpUid(String impUid);

//    public void cancelPayment(String merchantId) throws IamportResponseException, IOException;
}
