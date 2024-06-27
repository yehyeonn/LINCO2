package com.lec.spring.controller;

import com.lec.spring.domain.Reservation;
import com.lec.spring.domain.ReservationValidator;
import com.lec.spring.domain.SocializingValidator;
import com.lec.spring.domain.Venue;
import com.lec.spring.service.ReservationService;
import com.lec.spring.service.VenueService;
import jakarta.validation.Valid;
import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private VenueService venueService;

    @GetMapping("/write")
    public String write(@RequestParam("venue_id") Long venueId
            , @RequestParam("selectedDate") String selectedDate
            , Model model) {

        Venue venue = venueService.getVenueById(venueId);
        model.addAttribute("venue", venue);
        model.addAttribute("selectedDate", selectedDate);



        List<Reservation> reservations = reservationService.findByVenueAndDate(venueId, selectedDate);
        for (int i = 0; i < reservations.size(); i++) {
            System.out.println(reservations.get(i));
        }
        model.addAttribute("reservations", reservations);

        return "reservation/write";
    }


//    @PostMapping("/write")
//    public String writeOk(@RequestParam("venue_id") Long venueId
//            , @RequestParam("selectedDate") String selectedDate
//            , @Valid Reservation reservation
//            , BindingResult result
//            , Model model
//            , RedirectAttributes redirectAttributes) throws IOException {
//
//        Venue venue = venueService.getVenueById(venueId);
//        model.addAttribute("venue", venue);
//        model.addAttribute("selectedDate", selectedDate);
//
////        if (result.hasErrors()) {
////            System.out.println("에러네?");
////
////            redirectAttributes.addFlashAttribute("reservation_name", reservation.getReservation_name());
////            redirectAttributes.addFlashAttribute("email", reservation.getEmail());
////            redirectAttributes.addFlashAttribute("tel", reservation.getTel());
////            redirectAttributes.addFlashAttribute("reserve_start_time", reservation.getReserve_start_time());
//////            redirectAttributes.addFlashAttribute("reserve_end_time", reservation.getReserve_end_time());
////            System.out.println("출력이 될까?");
////
////            List<FieldError> errList = result.getFieldErrors();
////            for (FieldError err : errList) {
////                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
////            }
////
////            return "redirect:/reservation/write?venue_id=" + venueId + "&selectedDate=" + selectedDate;
////        }
//
////        model.addAttribute("result", reservationService.write(reservation));
//
//
//        List<Reservation> reservations = reservationService.findByVenueAndDate(venueId, selectedDate);
////        for (int i = 0; i < reservations.size(); i++) {
////            System.out.println(reservations.get(i));
////        }
//        model.addAttribute("reservations", reservations);
//
//
//        return "reservation/writeOk";
//    }


    @InitBinder("reservation")
    public void initBinder(WebDataBinder binder) {

        System.out.println("Reservation.initBinder() 호출");
        binder.setValidator(new ReservationValidator());
    }
}

