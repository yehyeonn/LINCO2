//package com.lec.spring.controller;
//
//
//import com.lec.spring.domain.User;
//import com.lec.spring.domain.UserSocializing;
//import com.lec.spring.service.UserSocializingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/userSocializing")
//public class UserSocializingController {
//    @Autowired
//    private UserSocializingService userSocializingService;
//
//    @PostMapping("/add")
//    public String addUserToSocializing(@RequestParam("user_id") Long userId, @RequestParam("socializing_id") Long socializingId, @RequestParam("role") String role, Model model) {
//        User user = new User();
//        user.setId(userId);
//
//        int result = userSocializingService.addUserToSocializing(user, socializingId, role);
//
//        if (result == 1) {
//            List<UserSocializing> userSocializings = userSocializingService.findBySocializingId(socializingId);
//            model.addAttribute("userSocializings", userSocializings);
//            return "socializing/detail";
//        } else {
//            model.addAttribute("error", "Failed to add user to socializing.");
//            return "socializing/detail";
//        }
//    }
//}
//
