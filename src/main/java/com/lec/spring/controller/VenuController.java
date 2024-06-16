package com.lec.spring.controller;


import com.lec.spring.domain.Venue;
import com.lec.spring.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}
