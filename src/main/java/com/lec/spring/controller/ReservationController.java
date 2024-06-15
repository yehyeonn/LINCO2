package com.lec.spring.controller;

import com.lec.spring.domain.Reservation;
import com.lec.spring.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/write")
    public void write() {
    }

    @PostMapping("/writeOk")
    public int createReservation(@RequestBody Reservation reservation) {
        return reservationService.write(reservation);
    }

    @GetMapping("/list")
    public List<Reservation> reservationList() {
        return reservationService.list();
    }

    @PostMapping("/update")
    public int updateReservation(@PathVariable("id") Long id, @RequestBody Reservation reservation) {
        reservation.setId(id);
        return reservationService.update(reservation);
    }
}
