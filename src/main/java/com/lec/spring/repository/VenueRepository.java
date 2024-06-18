package com.lec.spring.repository;

import com.lec.spring.domain.Venue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VenueRepository {

    List<Venue> findAll();

    List<Venue> findByCategory(@Param("category")String venue_category, @Param("fromRow") int fromRow, int pageRows);

    void insertVenue(Venue venue);

    List<Venue> selectFromRow(int from, int rows);

    int countAll();
}
