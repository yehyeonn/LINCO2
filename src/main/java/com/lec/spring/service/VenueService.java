package com.lec.spring.service;

import com.lec.spring.domain.Venue;

import java.util.List;

public interface VenueService {

    Venue findByCategory(String venue_category);

    List<Venue> findAll();
}
