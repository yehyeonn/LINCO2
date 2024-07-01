package com.lec.spring.controller;

import com.lec.spring.config.MvcConfiguration;
import com.lec.spring.domain.*;
//import com.lec.spring.repository.UserSocializingRepository;
import com.lec.spring.service.ReservationService;
import com.lec.spring.service.SocializingService;
import com.lec.spring.service.UserSocializingService;
import com.lec.spring.service.VenueService;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/socializing")
public class SocializingController {


    @Value("${app.upload.path}")
    private String uploadDir;


    @Autowired
    private SocializingService socializingService;

    @Autowired
    private UserSocializingService userSocializingService;

    @Autowired
    private VenueService venueService;

    @Autowired
    private ReservationService reservationService;


    @GetMapping("/write")
    public String write(@RequestParam(name = "venue_id", required = false) Long venue_id
            , @RequestParam(name = "reservation", required = false, defaultValue = "") String reservation
            , @RequestParam(name = "total_price", required = false) Long totalPrice
            , @RequestParam(name = "reservationDate", required = false) LocalDate reserveDate
            , @RequestParam(name = "reservationST", required = false) LocalTime reserveST
            , @RequestParam(name = "reserveET", required = false) LocalTime reserveET
            , @RequestParam(name = "merchantUid", required = false) String merchantUid
            , Model model, HttpSession session) {

        if (merchantUid != null) {
            merchantUid = (String) session.getAttribute("merchantUid");
        }

        Venue venue = (Venue) session.getAttribute("venue");
//        if (venue == null && venueId != null) {
//            venue = venueService.getVenueById(venueId);
//        }
        if (venue == null) {
            venue_id = null;

        } else {
            venue_id = venue.getId();
            reservation = venue.getVenue_name();
        }

        if (totalPrice == null) {
            totalPrice = (Long) session.getAttribute("totalPrice");
        }
        if (reserveDate == null) {
            reserveDate = (LocalDate) session.getAttribute("reserveDate");
        }
        if (reserveST == null) {
            reserveST = (LocalTime) session.getAttribute("reserveST");
        }
        if (reserveET == null) {
            reserveET = (LocalTime) session.getAttribute("reserveET");
        }


        // 새로운 작성 요청 시 세션 정보 초기화
        if (venue_id == null) {
            session.removeAttribute("venue");
            session.removeAttribute("totalPrice");
            session.removeAttribute("reserveDate");
            session.removeAttribute("reserveST");
            session.removeAttribute("reserveET");
            session.removeAttribute("merchantUid");

            // 세션이 초기화되었음을 확인하는 로그 추가
            System.out.println("세션 정보 초기화 완료");
        }


        List<String> category = socializingService.getAllCategories();
        Map<String, List<String>> detail_category = new HashMap<>();
        detail_category.put("운동", Arrays.asList("축구", "야구", "농구"));
        detail_category.put("공연", Arrays.asList("전시", "댄스", "영화"));
        detail_category.put("공부", Arrays.asList("컴퓨터", "영어", "수학"));


        model.addAttribute("venue_id", venue_id);
        model.addAttribute("venue_name", reservation);
        model.addAttribute("total_price", totalPrice);
        model.addAttribute("reserveDate", reserveDate);
        model.addAttribute("reserveST", reserveST);
        model.addAttribute("reserveET", reserveET);
        model.addAttribute("merchantUid", merchantUid);
        model.addAttribute("category", category);  // 카테고리 목록을 모델에 추가
        model.addAttribute("detail_category", detail_category);  // 소분류 목록을 모델에 추가

        return "socializing/write";
    }

    @GetMapping("/list")
    public void list(Integer page,
                     @RequestParam(name = "address", required = false, defaultValue = "") String address,
                     @RequestParam(name = "category", required = false, defaultValue = "") String category,
                     @RequestParam(name = "detailcategory", required = false, defaultValue = "") String detailcategory,
                     Model model) {

        socializingService.list(page, model, address, category, detailcategory);
    }


    @PostMapping("/write")
    public String writeOk(
            @Valid Socializing socializing
            , BindingResult result
            , @RequestParam("files") MultipartFile file
            , @RequestParam(name = "venue_id", required = false) Long venue_id
            , @RequestParam(name = "reservation", required = false) String reservation
            , @RequestParam(name = "total_price", required = false) Long total_price
            , @RequestParam(name = "reservationDate", required = false) LocalDate reservationDate
            , @RequestParam(name = "reservationST", required = false) LocalTime reservationST
            , @RequestParam(name = "reservationET", required = false) LocalTime reservationET
            , @RequestParam(name = "merchantUid", required = false, defaultValue = "") String merchantUid
            , UserSocializing userSocializing
            , Model model
            , RedirectAttributes redirectAttributes
    ) throws IOException {
        // 기본 이미지 경로 설정
        String imgPath = "upload/noimg.png"; // 기본 이미지 경로

        // 파일이 비어있지 않으면 업로드 처리
        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            imgPath = "upload/" + fileName;

            try {
                Path path = Paths.get(imgPath);
                Files.createDirectories(path.getParent());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socializing.getTotal_price() == null) {
            socializing.setTotal_price(total_price);
        }

        // 이미지 경로를 Socializing 객체에 설정
        socializing.setImg(imgPath);
        System.out.println("이미지 경로: " + imgPath); // 디버깅을 위한 로그 출력


        if (result.hasErrors()) {
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
            redirectAttributes.addFlashAttribute("venue_name", reservation);
            redirectAttributes.addFlashAttribute("reserveDate", reservationDate);
            redirectAttributes.addFlashAttribute("reserveST", reservationST);
            redirectAttributes.addFlashAttribute("reserveET", reservationET);
            redirectAttributes.addFlashAttribute("merchantUid", merchantUid);

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            // 폼 입력 값을 다시 보내기 위해 추가
            return "redirect:/socializing/write";
        }

        model.addAttribute("result", socializingService.write(socializing));
        userSocializingService.addUserToSocializing(userSocializing.getUser_id(), socializing.getId(), "MASTER");
        return "socializing/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        session.removeAttribute("venue");
        session.removeAttribute("totalPrice");
        session.removeAttribute("reserveDate");
        session.removeAttribute("reserveST");
        session.removeAttribute("reserveET");
        session.removeAttribute("merchantUid");
        List<UserSocializing> socializingMemberList = socializingService.socializingMemberList(id);
        List<Long> membersid = new ArrayList<>();
        UserSocializing socializingMaster = socializingService.findBySocializingMaster(id);
        Socializing socializing = socializingService.detail(id);
        int socializingcnt = socializingService.membercnt(id);
        String content = socializing.getContent().replace("\n", "<br>");
        for (UserSocializing e : socializingMemberList) {
            membersid.add(e.getUser().getId());
        }
        if (socializing.getVenue() == null) {
            Venue venue = new Venue();
            venue.setVenue_name("없음");
            socializing.setVenue(venue);
        }

        model.addAttribute("detailsocializing", socializing);
        model.addAttribute("membercnt", socializingcnt);
        model.addAttribute("members", socializingMemberList);
        model.addAttribute("content", content);
        model.addAttribute("Master", socializingMaster);
        model.addAttribute("Listnum", membersid);
        return "/socializing/detail";
    }

    @PostMapping("/detail")
    public String detailOk(@RequestParam(name = "user_id", required = false, defaultValue = "") Long userId
            , @RequestParam(name = "socializing_id", required = false, defaultValue = "") Long socializingId
            , Model model) {
        int result = userSocializingService.addUserToSocializing(userId, socializingId, "MEMBER");
        model.addAttribute("result", result);
        model.addAttribute("userSocializing", socializingId);
        return "socializing/detailOk";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
//        Socializing socializing = socializingService.selectById(id);
//        System.out.println(socializing);
        model.addAttribute("updatesocializing", socializingService.selectById(id));
        return "socializing/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @Valid Socializing socializing,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("socializing_title", socializing.getSocializing_title());
            redirectAttributes.addFlashAttribute("content", socializing.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getDefaultMessage());
            }
            return "redirect:/socializing/update/" + socializing.getId();
        }

        model.addAttribute("result", socializingService.update(socializing));
        return "socializing/updateOk";
    }

    @PostMapping("delete")
    public String deleteOk(Long id, Model model) {
        model.addAttribute("result", socializingService.deleteById(id));
        return "socializing/deleteOk";
    }

    @InitBinder("socializing")
    public void initBinder(WebDataBinder binder) {
        System.out.println("SocializingController.initBinder() 호출");
        binder.setValidator(new SocializingValidator());
    }

}
