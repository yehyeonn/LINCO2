package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.Club;
import com.lec.spring.domain.Socializing;
import com.lec.spring.domain.User;
import com.lec.spring.domain.Venue;
import com.lec.spring.service.ClubService;
import com.lec.spring.service.SocializingService;
import com.lec.spring.service.SocializingServiceImpl;
import com.lec.spring.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private VenueService venueService;
    @Autowired
    private SocializingService socializingService;
    @Autowired
    private ClubService clubService;


    @RequestMapping("/")
    public String home(Model model){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homepage(Long id, Model model) {
        List<Venue> venues = venueService.findAll();
        List<Club> clubs = clubService.getAllClubs();
        List<Socializing> socializing = socializingService.findAll();


        // 랜덤으로 섞기
        Collections.shuffle(venues);
        Collections.shuffle(socializing);
        Collections.shuffle(clubs);

        // 8개 제한, 부족할 경우 처리
        List<Venue> randomVenues = venues.stream().limit(8).collect(Collectors.toList());
        List<Socializing> randomSocializing = socializing.stream().limit(8).collect(Collectors.toList());
        List<Club> randomClubs = clubs.stream().limit(8).collect(Collectors.toList());

        List<Venue> leftVenues = randomVenues.subList(0, Math.min(4, randomVenues.size()));
        List<Venue> rightVenues = randomVenues.subList(Math.min(4, randomVenues.size()), randomVenues.size());

        List<Socializing> leftSocializing = randomSocializing.subList(0, Math.min(4, randomSocializing.size()));
        List<Socializing> rightSocializing = randomSocializing.subList(Math.min(4, randomSocializing.size()), randomSocializing.size());

        List<Club> leftClubs = randomClubs.subList(0, Math.min(4, randomClubs.size()));
        List<Club> rightClubs = randomClubs.subList(Math.min(4, randomClubs.size()), randomClubs.size());


        model.addAttribute("leftVenues", leftVenues);
        model.addAttribute("rightVenues", rightVenues);
        model.addAttribute("leftSocializing", leftSocializing);
        model.addAttribute("rightSocializing", rightSocializing);
        model.addAttribute("leftClubs", leftClubs);
        model.addAttribute("rightClubs", rightClubs);


        return "home";
    }


    //-------------------------------------------------------------------------
    // 현재 Authentication 보기 (디버깅 등 용도로 활용)
    @RequestMapping("/auth")
    @ResponseBody
    public Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 매개변수에 Authentication 을 명시해도 주입된다.  (인증후에만 가능)
    @RequestMapping("/userDetails")
    @ResponseBody
    public PrincipalDetails userDetails(Authentication authentication){
        return (PrincipalDetails) authentication.getPrincipal();
    }

    // @AuthenticationPrincipal 을 사용하여 로그인한 사용자 정보 주입받을수 있다.
    // org.springframework.security.core.annotation.AuthenticationPrincipal
    @RequestMapping("/user")
    @ResponseBody
    public User username(@AuthenticationPrincipal PrincipalDetails userDetails){
        return (userDetails != null) ? userDetails.getUser() : null;
    }

    // OAuth2 Client 를 사용하여 로그인 경우.
    // Principal 객체는 OAuth2User 타입으로 받아올수도 있다.
    // AuthenticatedPrincipal(I)
    //  └─ OAuth2AuthenticatedPrincipal(I)
    //       └─ OAuth2User (I)
    @RequestMapping("/oauth2")
    @ResponseBody
    public OAuth2User oauth2(Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        return oAuth2User;
    }

    @RequestMapping("/oauth2user")
    @ResponseBody
    public Map<String, Object> oauth2User(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return (oAuth2User != null) ? oAuth2User.getAttributes() : null;

    }


    @GetMapping("/home/venue/detail/{id}")
    public String getVenues(@PathVariable("id") Long id, Model model) {
        Venue venue = venueService.getVenueById(id);
        model.addAttribute("venue", venue);
        return "venue/detail";
    }

    @GetMapping("/home/socializing/detail/{id}")
    public String getSocializing(@PathVariable("id") Long id, Model model) {
        Socializing socializing = socializingService.detail(id);
        model.addAttribute("socializing", socializing);
        System.out.println(socializing);
        return "socializing/detail";
    }

    @GetMapping("/home/club/detail/{id}")
    public String getClub(@PathVariable("id") Long id, Model model) {
        Club club = clubService.getClubById(id);
        model.addAttribute("club", club);
        return "club/detail";
    }

}
