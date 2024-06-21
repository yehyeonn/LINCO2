package com.lec.spring.controller;
import com.lec.spring.domain.Socializing;
//import com.lec.spring.repository.UserSocializingRepository;
import com.lec.spring.domain.SocializingValidator;
import com.lec.spring.domain.UserSocializing;
import com.lec.spring.service.SocializingService;
import com.lec.spring.service.UserSocializingService;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/socializing")
public class SocializingController {

    @Autowired
    private SocializingService socializingService;

    @Autowired
    private UserSocializingService userSocializingService;


    @GetMapping("/write")
    public void write(Model model){
        List<String> category = socializingService.getAllCategories();
        Map<String, List<String>> detail_category = new HashMap<>();
        detail_category.put("운동", Arrays.asList("축구", "야구", "농구"));
        detail_category.put("공연", Arrays.asList("전시", "댄스", "영화"));
        detail_category.put("공부", Arrays.asList("컴퓨터", "영어", "수학"));

        model.addAttribute("category", category);  // 카테고리 목록을 모델에 추가
        model.addAttribute("detail_category", detail_category);  // 소분류 목록을 모델에 추가
    }

    @GetMapping("/list")
    public void list(Integer page,
                     @RequestParam(name = "address", required = false, defaultValue = "") String address,
                     @RequestParam(name = "category", required = false, defaultValue = "") String category,
                     @RequestParam(name = "detailcategory", required = false, defaultValue = "") String detailcategory,
                     Model model){

        socializingService.list(page, model, address, category, detailcategory);
    }


    @PostMapping("/write")
    public String writeOk(
            @Valid Socializing socializing
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttributes
            ){
        if(result.hasErrors()){
            System.out.println("에러났지롱~");
            System.out.println(socializing.getCategory() + "카테고리");
            System.out.println(socializing.getDetail_category() + "디테일_카테고리");
            redirectAttributes.addFlashAttribute("socializing_title", socializing.getSocializing_title());
            redirectAttributes.addFlashAttribute("category1", socializing.getCategory());  // 대분류
            redirectAttributes.addFlashAttribute("detail_category1", socializing.getDetail_category());  // 소분류
            redirectAttributes.addFlashAttribute("address", socializing.getAddress());
            redirectAttributes.addFlashAttribute("meeting_date", socializing.getMeeting_date());
            redirectAttributes.addFlashAttribute("meeting_time", socializing.getMeeting_time());
            redirectAttributes.addFlashAttribute("limit_num", socializing.getLimit_num());
            redirectAttributes.addFlashAttribute("content", socializing.getContent());
            redirectAttributes.addFlashAttribute("total_price", socializing.getTotal_price());
            redirectAttributes.addFlashAttribute("img", socializing.getImg());


            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }
        return "redirect:/socializing/write";
        }

    model.addAttribute("result", socializingService.write(socializing));
        return "socializing/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        Socializing socializing = socializingService.detail(id);
        int socializingcnt = socializingService.membercnt(id);
        List<UserSocializing> socializingMemberList = socializingService.socializingMemberList(id);

        String content = socializing.getContent().replace("\n","<br>");

        model.addAttribute("detailsocializing",socializing);
        model.addAttribute("membercnt",socializingcnt);
        model.addAttribute("members",socializingMemberList);
        model.addAttribute("content",content);
        return "/socializing/detail";
    }



    @InitBinder
    public void initBinder(WebDataBinder binder){
        System.out.println("SocializingController.initBinder() 호출");

        binder.setValidator(new SocializingValidator());
    }

}
