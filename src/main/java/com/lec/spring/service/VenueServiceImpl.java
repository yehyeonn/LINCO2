package com.lec.spring.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Venue;
import com.lec.spring.repository.VenueRepository;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VenueServiceImpl implements VenueService {

    private int WRITE_PAGES = 10;

    private int PAGE_ROWS = 12;

    private VenueRepository venueRepository;


    @Autowired
    public VenueServiceImpl(SqlSession sqlSession) {
        venueRepository = sqlSession.getMapper(VenueRepository.class);
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public Venue detail(Long id) {
        Venue venue = venueRepository.findById(id);

        return venue;
    }

    @Override
    public void saveVenue(Venue venue) {
        venueRepository.insertVenue(venue);
    }

    @Override
    public List<Venue> list(Integer page, Model model, String venue_category) {

        if (page == null || page < 1) {
            page = 1;
        }

        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("writePages");
        if (writePages == null) {
            writePages = WRITE_PAGES;
        }
        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if (pageRows == null) {
            pageRows = PAGE_ROWS;
        }
        session.setAttribute("page", page);

        long cnt = venueRepository.countByCategory(venue_category);
        int totalPage = (int) Math.ceil(cnt / (double) pageRows);

        int startPage = 0;
        int endPage = 0;

        List<Venue> list = null;

        if (cnt > 0) {
            if (page > totalPage) page = totalPage;

            int fromRow = (page - 1) * pageRows;

            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;

            if (endPage >= totalPage) endPage = totalPage;

            list = venueRepository.selectFromRow(fromRow, pageRows, venue_category);

            model.addAttribute("list", list);
            model.addAttribute("venue_category", venue_category);
        } else {
            page = 0;
        }

        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수

        // [페이징]
        model.addAttribute("url", U.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지

        return list;
    }
}
