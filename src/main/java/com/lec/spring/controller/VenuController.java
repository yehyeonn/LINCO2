package com.lec.spring.controller;


import com.lec.spring.domain.PublicReservationDTO;
import com.lec.spring.domain.Venue;
import com.lec.spring.service.VenueService;
import com.lec.spring.util.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/venue")
public class VenuController {

    private VenueService venueService;

    @Autowired
    public VenuController(VenueService venueService) {
        this.venueService = venueService;
    }


    @GetMapping("/list")
    public void venueList(Integer page, Model model) {
        venueService.list(page, model);
    }

    @GetMapping("/list/{venue_category}")
    public String VenueListByCategory(@PathVariable("venue_category") String venue_category, @RequestParam(name = "page", required = false) Integer page, Model model) {
        List<Venue> venueList = venueService.findByCategory(venue_category, page);

        if (page == null || page <= 1) {
            page = 1;
        }
        model.addAttribute("venueList", venueList);

        return "/venue/list";
    }

    @GetMapping("/api2")
    @ResponseBody
    public ResponseEntity<?> responseEntity() {
        RestTemplate rt = new RestTemplate();
        String url = "http://openapi.seoul.go.kr:8088/476753474c73777338374b73494b4b/json/ListPublicReservationInstitution/1/520/";
        ResponseEntity<PublicReservationDTO> response = rt.getForEntity(url, PublicReservationDTO.class);

        if (response.getBody() != null) {
            saveData(response.getBody());
        }
        return response;
    }

    @Transactional
    public void saveData(PublicReservationDTO publicReservationDTO) {
        List<PublicReservationDTO.Reservation> reservations = publicReservationDTO.getListPublicReservationInstitution().getRow();

        for (PublicReservationDTO.Reservation reservation : reservations) {
            Venue venue = new Venue();
            venue.setVenue_name(reservation.getVenue_name());
            venue.setAddress(reservation.getAddress());
            venue.setLimit_num(30);
            venue.setVenue_category(reservation.getVenue_category());
            venue.setInfo_tel(reservation.getInfo_tel());
            venue.setPrice(1000L);
            venue.setPosible_start_date("2024-07-09");
            venue.setPosible_end_date("2025-07-09");
            venue.setOpen_time("09:00:00");
            venue.setClose_time("18:00:00");
            venue.setImg(reservation.getImg());

            venueService.saveVenue(venue);
        }
    }
}
