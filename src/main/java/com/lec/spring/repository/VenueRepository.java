package com.lec.spring.repository;

import com.lec.spring.domain.Venue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VenueRepository {

    List<Venue> findAll();

//    List<Venue> findByCategory(Map<String, Object> params);

    void insertVenue(Venue venue);

    List<Venue> selectFromRow(int from, int rows, @Param("param3") String venue_category);

    int countAll();

    int countByCategory(String category);

    Venue findById(Long id);
}
