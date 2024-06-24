package com.lec.spring.controller;

import com.lec.spring.domain.Reservation;
import com.lec.spring.domain.Venue;
import com.lec.spring.service.ReservationService;
import com.lec.spring.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private VenueService venueService;

    @GetMapping("/write")
    public String write(@RequestParam("venue_id") Long venueId,
                        @RequestParam("selectedDate") String selectedDate,
                        Model model) {
        Venue venue = venueService.getVenueById(venueId);
        model.addAttribute("venue", venue);
        model.addAttribute("selectedDate", selectedDate);
        return "reservation/write";
    }
}

//@PostMapping("/writeOk")
//public int createReservation(@RequestBody Reservation reservation) {
//    return reservationService.write(reservation);
//}
//
//@GetMapping("/list")
//public List<Reservation> reservationList() {
//    return reservationService.list();
//}
//
//@PostMapping("/update")
//public int updateReservation(@PathVariable("id") Long id, @RequestBody Reservation reservation) {
//    reservation.setId(id);
//    return reservationService.update(reservation);
//}

