package com.lec.spring.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Venue;
import com.lec.spring.service.VenueService;
import com.nimbusds.jose.shaded.gson.JsonArray;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public VenuController(VenueService venueService) {
        this.venueService = venueService;
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

    @GetMapping("/api")
    @ResponseBody
    @Transactional
    public String fetchApi() {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;

        try {
            String apiUrl = "http://openapi.seoul.go.kr:8088/476753474c73777338374b73494b4b/json/ListPublicReservationInstitution/1/520/";
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String returnLine;

            while ((returnLine = br.readLine()) != null) {
                result.append(returnLine).append("\n\r");
            }

//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(result.toString());
//
//            if (rootNode.has("ListPublicReservationInstitution")) {
//                JsonNode venuesNode = rootNode.get("ListPublicReservationInstitution").get("row");
//
//                for (JsonNode venueNode : venuesNode) {
//                    Venue venue = new Venue();
//                    venue.setVenue_name(venueNode.get("SVCNM").asText());
//                    venue.setAddress(venueNode.get("AREANM").asText());
//                    venue.setLimit_num(venueNode.get("REVSTDDAY").asInt());
//                    venue.setVenue_category(venueNode.get("SVCNM").asText());
//                    venue.setInfo_tel(venueNode.get("TELNO").asText());
//                    venue.setPrice(10000L);
//                    venue.setPosible_start_date(venueNode.get("SVCOPNBGNDT").asText());
//                    venue.setPosible_end_date(venueNode.get("SVCOPNENDDT").asText());
//                    venue.setOpen_time(venueNode.get("V_MIN").asText());
//                    venue.setClose_time(venueNode.get("V_MAX").asText());
//                    venue.setImg(venueNode.get("IMGURL").asText());
//
//                    venueService.saveVenue(venue);
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result.toString();
    }
}
