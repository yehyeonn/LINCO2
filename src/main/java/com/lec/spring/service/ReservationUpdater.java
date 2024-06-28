package com.lec.spring.service;

import com.lec.spring.domain.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 매일 자정 예약 내역을 확인하고 업데이트하는 백업 스케쥴러
@Component
public class ReservationUpdater {

    private ReservationService reservationService;

    @Autowired
    public ReservationUpdater(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(cron = "0 0 * * * *") // 매일 자정에 실행
    public void updateResrevationsStatus() {
        List<Reservation> reservations = reservationService.findPayedReservation();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(Reservation reservation : reservations) {
            LocalDate reservationsDate = reservation.getReserve_date();

            if(reservationsDate.isBefore(LocalDate.now())) {
                reservation.setStatus("DONE");
                reservationService.update(reservation);
            }
        }
    }

//    public void updateExpiredReservationStatus(){
//        List<Reservation> reservations = reservationService.findPayedReservation();
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        for(Reservation reservation : reservations) {
//            LocalDate reservationsDate = reservation.getReserve_date();
//
//            if(reservationsDate.isBefore(LocalDate.now())) {
//                reservation.setStatus("DONE");
//                reservationService.update(reservation);
//            }
//        }
//    }
}
