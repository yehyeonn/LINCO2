package com.lec.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/culture")
public class CultureController {

    @GetMapping("/detail")
    public String culture() {
        return "culture/detail"; // 'culture/detail.html'을 반환
    }
}
