package com.lec.spring.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {

    public HomeController(){
        System.out.println("diq!!!!!");
    }

    @RequestMapping("/")
    public String home(Model model) {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public void home(){}

    @RequestMapping("/auth")
    @ResponseBody
    public Authentication auth() {

        // TODO
        return null;
    }
}
