package com.lec.spring.controller;
import com.lec.spring.domain.Socializing;
//import com.lec.spring.repository.UserSocializingRepository;
import com.lec.spring.service.SocializingService;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/socializing")
public class SocializingController {

    @Autowired
    private SocializingService socializingService;
//    private UserSocializingRepository userSocializingRepository;


    @GetMapping("/write")
    public void write(){}

    @GetMapping("/list")
    public void list(Integer page,
                     @RequestParam(name = "address", required = false, defaultValue = "") String address,
                     @RequestParam(name = "category", required = false, defaultValue = "") String category,
                     @RequestParam(name = "detailcategory", required = false, defaultValue = "") String detailcategory,
                     Model model){

        socializingService.list(page, model, address, category, detailcategory);
    }


//    @PostMapping("/write")
//    public String writeOk(
//            @Valid Socializing socializing
//            , BindingResult result
//            , Model model
//            , RedirectAttributes redirectAttributes
//            ){
//        if(result.hasErrors()){
//            // TODO
//        return "redirect:/socializing/write";
//        }
//
//    model.addAttribute("result", socializingService.write(socializing));
//        return "socializing/writeOk";
//    }

//    @GetMapping("detail/{id}")
//    public String detail(@PathVariable Long id, Model model){
//
//        Socializing socializing = socializingService.detail(id);
//        model.addAttribute("socializing", socializing);
//
//        return "socializing/detail";
//    }

//    @PostMapping("/update")
//    public String updateOk(
//            @Valid Socializing socializing
//            , BindingResult result
//            , Model model
//            , RedirectAttributes redirectAttributes
//    ){
//        if(result.hasErrors()){
//            //TODO
//
//            return "redirect:/socializing/update" + socializing.getId();
//        }
//
//        model.addAttribute("result", socializingService.update(socializing));
//        return "socializing/updateOk";
//    }
//
//    @PostMapping("/delete")
//    public String deleteOk(Long id, Model model){
//        model.addAttribute("result", socializingService.deleteById(id));
//        return "socializing/deleteOk";
//    }

    //페이징
    //pageRows 변경시 동작
//    @PostMapping("/pageRows")
//    public String pageRows(Integer page, Integer pageRows) {
//        U.getSession().setAttribute("pageRows",pageRows);
//        System.out.println("여기는 언제?");
//        return "redirect:/socializing/list?page=" + page;
//
//    }


}
