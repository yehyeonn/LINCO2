package com.lec.spring.repository;

import com.lec.spring.domain.Venue;

import java.util.List;

public interface VenueRepository {

    List<Venue> findAll();

    List<Venue> findByCategory(String venueCategory);
}
