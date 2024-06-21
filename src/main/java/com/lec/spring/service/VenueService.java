package com.lec.spring.service;

import com.lec.spring.domain.Venue;
import org.springframework.ui.Model;

import java.util.List;

public interface VenueService {


    List<Venue> findAll();

    Venue detail(Long id);

    void saveVenue(Venue venue);

    List<Venue> list(Integer page, Model model, String venue_category);

    Venue getVenueById(Long id);

}
