package com.lec.spring.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Venue;
import com.lec.spring.repository.VenueRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {

    private VenueRepository venueRepository;

    @Autowired
    public VenueServiceImpl(SqlSession sqlSession) {
        venueRepository = sqlSession.getMapper(VenueRepository.class);
    }

    @Override
    public List<Venue> findByCategory(String venue_category) {
        return venueRepository.findByCategory(venue_category);
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    @Override
    @Transactional
    public void saveVenue(Venue venue) {
        venueRepository.insertVenue(venue);
    }
}
