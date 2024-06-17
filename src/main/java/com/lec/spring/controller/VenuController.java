package com.lec.spring.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.PublicReservationDTO;
import com.lec.spring.domain.Venue;
import com.lec.spring.repository.VenueRepository;
import com.lec.spring.service.ApiService;
import com.lec.spring.service.VenueService;
import com.nimbusds.jose.shaded.gson.JsonArray;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/venue")
public class VenuController {

    private VenueService venueService;
    private ApiService apiService;
    @Autowired
    public VenuController(VenueService venueService, ApiService apiService) {
        this.venueService = venueService;
        this.apiService = apiService;
    }


    @GetMapping("/list")
    public String venueList(Model model) {
        List<Venue> venueList = venueService.findAll();
        model.addAttribute("venueList", venueList);
        return "/venue/list";
    }

    @GetMapping("/list/{category}")
    public String getVenueListByCategory(@PathVariable("category") String category, Model model) {
        List<Venue> venueList = venueService.findByCategory(category);
        model.addAttribute("venueList", venueList);
        return "/venue/list";
    }

    @GetMapping("/api2")
    @ResponseBody
    public ResponseEntity<?> 우솝(){
        RestTemplate rt = new RestTemplate();
        String url = "http://openapi.seoul.go.kr:8088/476753474c73777338374b73494b4b/json/ListPublicReservationInstitution/1/520/";
        ResponseEntity<PublicReservationDTO> response = rt.getForEntity(url, PublicReservationDTO.class);
        return response;
    }

}
