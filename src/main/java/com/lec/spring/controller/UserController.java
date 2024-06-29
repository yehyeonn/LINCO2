package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.*;
import com.lec.spring.repository.ClubUserListRepository;
import com.lec.spring.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${app.upload.path}")
    private String uploadDir;

    private UserService userService;
    private UserSocializingService userSocializingService;
    private ReservationService reservationService;



    @Autowired
    public void setUserService(UserService userService, ReservationService reservationService, UserSocializingService userSocializingService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.userSocializingService = userSocializingService;
    }

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String registerOk(@Valid User user,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttrs){

        // 검증 에러가 있었다면 redirect 한다
        if(result.hasErrors()){
//            redirectAttrs.addFlashAttribute("username", user.getUsername());
//            redirectAttrs.addFlashAttribute("name", user.getName());
//            redirectAttrs.addFlashAttribute("email", user.getEmail());
//
//            List<FieldError> errList = result.getFieldErrors();
//            for(FieldError err : errList) {
//                redirectAttrs.addFlashAttribute("error", err.getCode());  // 가장 처음에 발견된 에러를 담아ㅣ 보낸다
//                break;
//            }

            return "redirect:/user/register";
        }

        // 에러 없었으면 회원 등록 진행
        String page = "/user/registerOk";
        int cnt = userService.register(user);
        model.addAttribute("result", cnt);
        return page;
    }

//    @Autowired
//    UserValidator userValidator;
//
//    @InitBinder
//    public void initBinder(WebDataBinder binder){
//        binder.setValidator(userValidator);
//    }

    @GetMapping("/login")
    public void login(){}

    // onAuthenticationFailure 에서 로그인 실패시 forwarding 용
    // request 에 담겨진 attribute 는 Thymeleaf 에서 그대로 표현 가능.
    @PostMapping("/loginError")
    public String loginError() {
        return "user/login";
    }


    // 마이페이지
    @GetMapping("/my_page")
    public String my_page(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){

        if(principalDetails == null){ // 인증되지 않은 사용자 로그인 페이지로 리다이렉션
            return "redirect:/user/login";
        }

        User user = principalDetails.getUser();
        List<ClubUserList> userClubs = userService.getUserClubs(user.getId());
        List<UserSocializing> userSocializings = userSocializingService.findByUserSocializingId(user.getId());
        List<Reservation> userReservations = reservationService.findByUserId(user.getId());
        System.out.println(userSocializings);

        userReservations.forEach(reservation -> {
            System.out.println(reservation);
        });


        // 마이페이지 클럽목록 기본이미지
        for (ClubUserList clubUserList : userClubs) {
            Club club = clubUserList.getClub();
            if ("upload/Default.png".equals(club.getRepresentative_picture())) {
                String imgPath = "noimg.png";
                club.setRepresentative_picture(imgPath);
            }
        }

        model.addAttribute("userClubs", userClubs);
        model.addAttribute("userSocializings", userSocializings);
        model.addAttribute("userReservations", userReservations);
        model.addAttribute("user", user);

        return "user/my_page";
    }

    @PostMapping("/update")
    public String update(
            @RequestParam("files") MultipartFile file,
            User user,
            Model model) throws IOException {
        // 기본 이미지 경로 설정
        String imgPath = "upload/profile_img.jpg"; // 기본 이미지 경로

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

        // 이미지 경로를 Socializing 객체에 설정
        user.setProfile_picture(imgPath);
        userService.update(user);

        // 업데이트된 유저 정보 가져오기
        User updateUser = userService.findByUserId(user.getId());

        // 마이페이지에 필요한 데이터 다시 설정
        List<ClubUserList> userClubs = userService.getUserClubs(updateUser.getId());
        List<Reservation> userReservations = reservationService.findByUserId(updateUser.getId());
        List<UserSocializing> userSocializings = userSocializingService.findByUserSocializingId(updateUser.getId());

        // 모델에 데이터 추가
        model.addAttribute("userClubs", userClubs);
        model.addAttribute("userReservations", userReservations);
        model.addAttribute("userSocializings", userSocializings);
        model.addAttribute("User", updateUser);

        return "user/my_page";
    }
}
