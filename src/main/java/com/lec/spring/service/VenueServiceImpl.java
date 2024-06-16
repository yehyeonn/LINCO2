package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Venue;
import com.lec.spring.repository.VenueRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueServiceImpl implements VenueService {

    private VenueRepository venueRepository;

    @Autowired
    public VenueServiceImpl(SqlSession sqlSession){
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

}
