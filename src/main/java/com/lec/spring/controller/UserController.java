package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.*;
import com.lec.spring.repository.ClubUserListRepository;
import com.lec.spring.service.*;
import com.lec.spring.service.*;
import com.lec.spring.util.U;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.AuthData;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Schedule;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import retrofit2.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${app.upload.path}")
    private String uploadDir;

    private IamportService iamportService;
    private UserService userService;
    private UserSocializingService userSocializingService;
    private ReservationService reservationService;

    @Autowired
    public void setUserService(UserService userService, ReservationService reservationService, UserSocializingService userSocializingService, IamportService iamportService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.userSocializingService = userSocializingService;
        this.iamportService = iamportService;
    }

    @GetMapping("/register")
    public void register() {
    }

    @PostMapping("/register")
    public String registerOk(@Valid User user,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttrs) {

        // 검증 에러가 있었다면 redirect 한다
        if (result.hasErrors()) {
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
    public void login() {
    }

    // onAuthenticationFailure 에서 로그인 실패시 forwarding 용
    // request 에 담겨진 attribute 는 Thymeleaf 에서 그대로 표현 가능.
    @PostMapping("/loginError")
    public String loginError() {
        return "user/login";
    }


    // 마이페이지
    @GetMapping("/my_page")
    public String my_page(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        if (principalDetails == null) { // 인증되지 않은 사용자 로그인 페이지로 리다이렉션
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

    @PostMapping("/cancel")
    @ResponseBody
    public String cancelPayment(@RequestBody Map<String, Object> request) {
        String impUid = (String) request.get("imp_uid");
        String merchantUid = (String) request.get("merchant_uid");
        int amount = (int) request.get("total_price");
        String reason = (String) request.get("reason");

        System.out.println("impUid : " + impUid);
        Reservation reservation = reservationService.findByImpUid(impUid);
        if (reservation != null) {
            reservation.setStatus("CANCELED");
            reservationService.update(reservation);
        } else {
            return "예약 정보를 찾을 수 없습니다.";
        }
    // 여기까지는 됐는데 try-catch에서 실패 뜸 => cancelPayment 가 문제?
        try {
            boolean isSuccess = iamportService.cancelPayment(impUid, merchantUid, amount, reason);
            if (isSuccess) {
                return "결제 취소가 완료되었습니다.";
            } else {
                return "결제 취소 실패";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "서버 오류가 발생했습니다.";
        }
    }
//    @PostMapping("/cancel")
//    public ResponseEntity<String> cancelPayment(@RequestBody Reservation reservation, @RequestParam String canReason) {
//        try {
//            // 여기서 cancelRequest 객체를 사용하여 결제 취소 처리를 수행
//            String impUid = reservation.getImpUid();
//            String merchantUid = reservation.getMerchantUid();;
//            Long total_price = reservation.getTotal_price();
//            String reason = canReason;
//
//
//            // 결제 취소 로직을 수행하고 성공적인 응답을 반환
//            // 예를 들어, 결제 취소가 성공적으로 이루어졌다면
//            return ResponseEntity.ok().body("결제 취소가 완료되었습니다.");
//        } catch (Exception e) {
//            // 결제 취소 과정에서 예외가 발생하면 실패 응답을 반환
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 취소 중 오류가 발생했습니다.");
//        }
//    }
}


//    public ResponseEntity<String> cancel(@RequestBody Map<String, Object> payload) {
//        try {
//            // Payload에서 데이터 추출
//            String merchantUid = payload.get("merchant_uid").toString();
//            Long totalPrice = Long.parseLong(payload.get("cancel_request_amount").toString());
//            String reason = payload.get("reason").toString();
//
//            // 예약 정보를 DB에서 가져옵니다.
//            Reservation existingReservation = reservationService.findByMerchant(merchantUid);
//
//            if (existingReservation != null) {
//                // 아임포트 클라이언트 초기화
//                IamportClient iamportClient = iamportService.getIamportClient();
//
//                // 결제 정보 조회
//                IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(merchantUid);
//
//                if (paymentResponse.getResponse() != null) {
//                    Payment payment = paymentResponse.getResponse();
//
//                    // 결제 상태 확인
//                    if ("paid".equals(payment.getStatus())) {
//                        // 결제 취소 요청 데이터 생성
//                        CancelData cancelData = new CancelData(merchantUid, true, BigDecimal.valueOf(totalPrice));
//                        cancelData.setReason(reason);
//
//                        // 아임포트 API를 사용하여 결제 취소 요청
//                        IamportResponse<Payment> iamportResponse = iamportClient.cancelPaymentByImpUid(cancelData);
//
//                        if (iamportResponse.getResponse() != null) {
//                            // 결제 취소 성공 시 예약 상태 업데이트
//                            existingReservation.setStatus("CANCELED");
//                            reservationService.update(existingReservation);
//
//                            return ResponseEntity.ok("결제 취소 완료");
//                        } else {
//                            // 결제 취소 실패 시
//                            return ResponseEntity.status(500).body("결제 취소 중 오류 발생: " + iamportResponse.getMessage());
//                        }
//                    } else {
//                        // 이미 취소된 상태이거나 취소할 수 없는 상태인 경우
//                        return ResponseEntity.status(400).body("취소할 수 없는 결제 상태입니다: " + payment.getStatus());
//                    }
//                } else {
//                    // 결제 정보 조회 실패 시
//                    return ResponseEntity.status(500).body("결제 정보 조회 실패: " + paymentResponse.getMessage());
//                }
//            } else {
//                // 예약 정보를 찾을 수 없는 경우
//                return ResponseEntity.status(404).body("예약을 찾을 수 없습니다.");
//            }
//        } catch (NumberFormatException e) {
//            // 숫자 변환 오류 처리
//            return ResponseEntity.status(400).body("숫자 변환 오류: " + e.getMessage());
//        } catch (Exception e) {
//            // 기타 예외 처리
//            return ResponseEntity.status(500).body("결제 취소 중 오류 발생: " + e.getMessage());
//        }
//    }
//}
