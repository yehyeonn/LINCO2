package com.lec.spring.controller;

import com.lec.spring.domain.Club;
import com.lec.spring.domain.ClubValidator;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/club")
public class ClubController {

    @Autowired
    private ClubService clubService;


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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("ClubController.initBinder() 호출");
        binder.setValidator(new ClubValidator());
    }

}

