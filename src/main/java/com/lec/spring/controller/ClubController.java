package com.lec.spring.controller;

import com.lec.spring.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/club")
public class ClubController {

    @Autowired
    private ClubService clubService;

    public ClubController() {
        System.out.println("ClubController() 생성");
    }

    @GetMapping("/create")
    public void create(){}


}
