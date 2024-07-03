package com.lec.spring.controller;

import com.lec.spring.domain.Reservation;
import com.lec.spring.domain.ReservationValidator;
import com.lec.spring.domain.SocializingValidator;
import com.lec.spring.domain.Venue;
import com.lec.spring.service.IamportService;
import com.lec.spring.service.ReservationService;
import com.lec.spring.service.UserService;
import com.lec.spring.service.VenueService;
import com.lec.spring.util.U;
import com.nimbusds.oauth2.sdk.Response;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {


    private final IamportService iamportService;
    private final ReservationService reservationService;
    private final VenueService venueService;

    @Autowired
    public ReservationController(IamportService iamportService, ReservationService reservationService, VenueService venueService) {
        this.iamportService = iamportService;
        this.reservationService = reservationService;
        this.venueService = venueService;
    }
    @GetMapping("/write")
    public String write(@RequestParam("venue_id") Long venueId
            , @RequestParam("selectedDate") String selectedDate
            , Model model) {

        Venue venue = venueService.getVenueById(venueId);
        model.addAttribute("venue", venue);
        model.addAttribute("selectedDate", selectedDate);


        List<Reservation> reservations = reservationService.findByVenueAndDate(venueId, selectedDate);
        System.out.println(reservations);
        model.addAttribute("reservations", reservations);

        return "reservation/write";
    }


    @PostMapping("/savePayment")
    public ResponseEntity<String> savePayment(@RequestBody Reservation reservation) {
        // 로그에 데이터 출력
        System.out.println("Received reservation: " + reservation);

        // 예약 정보를 데이터베이스에 저장하는 로직
        reservationService.write(reservation);

        HttpSession session = U.getSession();
        session.setAttribute("venue", reservation.getVenue());
        session.setAttribute("totalPrice", reservation.getTotal_price());
        session.setAttribute("reserveDate", reservation.getReserve_date());
        session.setAttribute("reserveST", reservation.getReserve_start_time());
        session.setAttribute("reserveET", reservation.getReserve_end_time());
        session.setAttribute("merUid", reservation.getMerchantUid());
        session.setAttribute("impUid", reservation.getImpUid());
//        System.out.println("베뉴 정보가 들어올까요~?" + reservation.getVenue());

        return ResponseEntity.ok("디비디비딥!");
    }



    @GetMapping("/validate/{imp_uid}")
    public ResponseEntity<String> validatePayment(@PathVariable String imp_uid,
                                             @RequestParam String merchant_uid,
                                             @RequestParam Long amount) {

        // 결제 검증 로직
        // 결제 정보 조회 및 검증
        return ResponseEntity.ok().body("결제 정보 검증 성공");
    }
}

