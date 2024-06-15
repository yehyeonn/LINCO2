package com.lec.spring.controller;


import com.lec.spring.domain.Venue;
import com.lec.spring.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/venue")
public class VenuController {

    @Autowired
    private VenueService venueService;

    public VenuController() {
        System.out.println("VenuController 호출");
    }

    @GetMapping("/list")
    public String venueList(Model model) {
        List<Venue> venue = venueService.findAll();
        model.addAttribute("venue", venue);
        return "venue/list";
    }

    @GetMapping("/list/{category}")
    public String getVenueListByCategory(@PathVariable("category") String category, Model model) {
        Venue venue = venueService.findByCategory(category);
        model.addAttribute("venue", venue);
        return "venue/list";
    }
}
