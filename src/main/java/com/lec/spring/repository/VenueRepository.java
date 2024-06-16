package com.lec.spring.repository;

import com.lec.spring.domain.Venue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VenueRepository {

    List<Venue> findAll();

    List<Venue> findByCategory(String venueCategory);

    void insertVenue(Venue venue);
}
