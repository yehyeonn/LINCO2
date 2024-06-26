package com.lec.spring.controller;

import com.lec.spring.domain.*;
import com.lec.spring.service.ClubService;
import com.lec.spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/club")
public class ClubController {

    @Value("upload")
    private String uploadDir;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserService userService;


    public ClubController() {
        System.out.println("ClubController() 생성");
    }

    // 클럽 생성 페이지를 표시
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("club", new Club());
        return "club/create";
    }

    // 클럽 생성 요청 처리
    @PostMapping("/create")
    public String createOk(
//            @RequestParam Map<String, MultipartFile> files, // 첨부파일들 <name, file>
            @Valid Club club,
            BindingResult result,
            Model model,    // 매개변수 선언시 BindingResult 보다 Model 을 뒤에 두어야 한다.
            RedirectAttributes redirectAttrs   // redirect 시 넘겨줄 값들을 담는 객체
    ) {

        // 유효성 검사에서 에러가 발생한 경우
        if (result.hasErrors()) {

            // 기존에 입력했던 값들을 다시 보여주기 위해 redirectAttrs에 추가
            redirectAttrs.addFlashAttribute("name", club.getName());
            redirectAttrs.addFlashAttribute("clubCategory", club.getCategory());
            redirectAttrs.addFlashAttribute("detailCategory", club.getDetail_category());
            redirectAttrs.addFlashAttribute("intro", club.getIntro());
            redirectAttrs.addFlashAttribute("content", club.getContent());

            // 에러 메시지를 추가
            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/club/create";
        }

        // 클럽 생성 로직 실행
        model.addAttribute("result", clubService.createClub(club));
        return "club/createOk";
    }

    @GetMapping("/list")
    private String list(
            Integer page,
            @RequestParam(name="clubCategory", required = false, defaultValue = "") String category,
            @RequestParam(name= "detailCategory", required = false, defaultValue = "") String detailcategory,
            Model model){

        List<Club> clubs = clubService.list(page, model, category, detailcategory);

        model.addAttribute("clubs", clubs);
        System.out.println("clubs 갯수 : " + clubs.size());
        System.out.println("clubs : " + clubs.toString());

        return "club/list";
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        // 클릭한 클럽 객체 -> 대표사진, 클럽이름, 상세종목, 소개, 상세내용
        Club club = clubService.getClubById(id);
        System.out.println("club: " + club);
        // 클럽의 멤버 리스트 -> user_id, club_id, role
        List<ClubUserList> clubMemberList= clubService.getClubMemberList(id);
        System.out.println("clubMemberList: "+ clubMemberList);

        // 각 클럽 멤버의 사용자 이름을 찾기 위한 리스트
        List<User> clubMembersWithUsernames = new ArrayList<>();

        for (ClubUserList clubUser : clubMemberList) {
            User user = userService.findById(clubUser.getUser_id());
            clubMembersWithUsernames.add(user);
        }
        System.out.println("clubMembersWithUsernames: "+ clubMembersWithUsernames);


        // 클럽장
        ClubUserList clubMaster = clubService.findClubMaster(id);
        System.out.println("clubMaster :" + clubMaster);

        // 멤버 수 -> 현재인원
        int memberCount = clubService.getClubMemberCount(id);
        System.out.println("memberCount: " + memberCount);

        model.addAttribute("club", club);
        model.addAttribute("clubMemberList", clubMemberList);
        model.addAttribute("clubMembersWithUsernames", clubMembersWithUsernames);
        model.addAttribute("clubMaster", clubMaster);
        model.addAttribute("memberCount", memberCount);


        return "/club/detail";

    }

    @InitBinder("club")
    public void initBinder(WebDataBinder binder) {
        System.out.println("ClubController.initBinder() 호출");
        binder.setValidator(new ClubValidator());
    }

}

