package com.lec.spring.controller;

import com.lec.spring.domain.*;
import com.lec.spring.service.*;
import com.lec.spring.service.BoardService;
import com.lec.spring.service.ClubService;
import com.lec.spring.service.ClubUserListService;
import com.lec.spring.service.UserService;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/club")
public class ClubController {

    @Value("upload")
    private String uploadDir;

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubUserListService clubUserListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private BoardService boardService;


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
            @RequestParam("files") MultipartFile file,
            @Valid Club club,
            BindingResult result,
            Model model,    // 매개변수 선언시 BindingResult 보다 Model 을 뒤에 두어야 한다.
            RedirectAttributes redirectAttrs   // redirect 시 넘겨줄 값들을 담는 객체
    ) throws IOException {
        // 기본 이미지 경로 설정
        String imgPath = "noimg.png"; // 기본 이미지 경로

        // 파일이 비어있지 않으면 업로드 처리
        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            imgPath = fileName;

            try {
                Path path = Paths.get("upload/" + imgPath);
                Files.createDirectories(path.getParent());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 이미지 경로를 Socializing 객체에 설정
        club.setRepresentative_picture(imgPath);
        System.out.println("이미지 경sh로: " + imgPath); // 디버깅을 위한 로그 출력


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

    // 클럽 이름 중복 확인 요청 처리
    @PostMapping("/checkDuplicate")
    @ResponseBody
    public Map<String, Boolean> checkDuplicate(@RequestParam("clubName") String clubName) {
        Map<String, Boolean> response = new HashMap<>();

        // 클럽 이름 중복 체크
        boolean isDuplicate = clubService.isClubNameExists(clubName);

        response.put("duplicate", isDuplicate);
        return response;
    }

    @GetMapping("/list")
    private String list(
            Integer page,
            @RequestParam(name = "clubCategory", required = false, defaultValue = "") String category,
            @RequestParam(name = "detailCategory", required = false, defaultValue = "") String detailcategory,
            Model model) {

        List<Club> clubs = clubService.list(page, model, category, detailcategory);

        model.addAttribute("clubs", clubs);
        System.out.println("clubs 갯수 : " + clubs.size());
        System.out.println("clubs : " + clubs.toString());

        return "club/list";
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        // 클릭한 클럽 객체 -> 대표사진, 클럽이름, 상세종목, 소개, 상세내용
        Club club = clubService.getClubById(id);
        System.out.println("club: " + club);

        // 클럽의 멤버 리스트 -> user_id, club_id, role, user
        List<ClubUserList> clubMemberList = clubUserListService.clubuserlist(id);
        System.out.println("clubMemberList: " + clubMemberList);

        // 클럽장 -> user_id, club_id, role, user
        ClubUserList clubMaster = clubService.findClubMaster(id);
        System.out.println("clubMaster :" + clubMaster);

        // clubMemberList에서 user_id만 추출하여 리스트로 만들기 (클럽장 제외)
        List<Long> userIds = clubMemberList.stream()
                .map(clubUserList -> clubUserList.getUser().getId())
                .filter(user_id -> !user_id.equals(clubMaster.getUser_id())) // 클럽장 제외
                .collect(Collectors.toList());

        System.out.println("userIds: " + userIds);

        // 클럽장과 동일한 user_id를 가진 항목을 제외한 리스트 만들기
        List<ClubUserList> filteredClubMemberList = clubMemberList.stream()
                .filter(clubUserList -> !clubUserList.getUser().getId().equals(clubMaster.getUser().getId()))
                .collect(Collectors.toList());

        System.out.println("filteredClubMemberList: " + filteredClubMemberList);


        // 멤버 수 -> 현재인원
        int memberCount = clubService.getClubMemberCount(id);
        System.out.println("memberCount: " + memberCount);

        model.addAttribute("club", club);
        model.addAttribute("filteredClubMemberList", filteredClubMemberList);
        model.addAttribute("clubMaster", clubMaster);
        model.addAttribute("userIds", userIds);
        model.addAttribute("memberCount", memberCount);

        return "/club/detail";
    }

    @GetMapping("board/{id}")
    public String board(@PathVariable Long id
            , @RequestParam(name = "title", required = false, defaultValue = "") String title
            , Integer page
            , Model model) {
        // 클릭한 클럽 객체 -> 대표사진, 클럽이름, 상세종목, 소개, 상세내용
        Club club = clubService.getClubById(id);
        System.out.println("club: " + club);

        //게시판의 리스트 및 페이지
        boardService.clubPostList(id, page, model,title);

        // 클럽의 멤버 리스트 -> user_id, club_id, role, user
        List<ClubUserList> clubMemberList = clubUserListService.clubuserlist(id);
        System.out.println("clubMemberList: " + clubMemberList);

        // 클럽장 -> user_id, club_id, role, user
        ClubUserList clubMaster = clubService.findClubMaster(id);
        System.out.println("clubMaster :" + clubMaster);

        // clubMemberList에서 user_id만 추출하여 리스트로 만들기 (클럽장 제외)
        List<Long> userIds = clubMemberList.stream()
                .map(clubUserList -> clubUserList.getUser().getId())
                .filter(user_id -> !user_id.equals(clubMaster.getUser_id())) // 클럽장 제외
                .collect(Collectors.toList());

        System.out.println("userIds: " + userIds);

        // 클럽장과 동일한 user_id를 가진 항목을 제외한 리스트 만들기
        List<ClubUserList> filteredClubMemberList = clubMemberList.stream()
                .filter(clubUserList -> !clubUserList.getUser().getId().equals(clubMaster.getUser().getId()))
                .collect(Collectors.toList());

        System.out.println("filteredClubMemberList: " + filteredClubMemberList);


        // 멤버 수 -> 현재인원
        int memberCount = clubService.getClubMemberCount(id);
        System.out.println("memberCount: " + memberCount);

        model.addAttribute("club", club);
        model.addAttribute("filteredClubMemberList", filteredClubMemberList);
        model.addAttribute("clubMaster", clubMaster);
        model.addAttribute("userIds", userIds);
        model.addAttribute("memberCount", memberCount);

        return "/club/board";
    }

    @GetMapping("board/detail/{id}")
    public String boardDetail(@PathVariable Long id, Model model){
        Board board = boardService.detail(id);
        List<Board> clubBoards = clubService.getClubBoard(id);

//        String content = club.getContent().replace("\n", "<br>");

        List<Comment> comments = commentService.list(id).getList();

        int cnt = commentService.list(id).getCount();

        List<Attachment> attachments = attachmentService.findByAttachment(id);

        model.addAttribute("board", board);
        model.addAttribute("clubBoards", clubBoards);
        model.addAttribute("attachments", attachments);
        model.addAttribute("cnt", cnt);
        model.addAttribute("comments", comments);
//        model.addAttribute("content", content);

        return "club/boardDetail";
    }


    @PostMapping("/join")
    public String join(@RequestParam(name = "user_id", required = false, defaultValue = "") Long user_id
            , @RequestParam(name = "club_id", required = false, defaultValue = "") Long club_id
            , Model model) {
        int result = clubService.addMemberToClub(user_id, club_id);
        model.addAttribute("result", result);
        model.addAttribute("club_id", club_id);
        return "/club/joinOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model) {
        model.addAttribute("result", clubService.deleteById(id));
        return "/club/deleteOk";
    }


    @PostMapping("/out")
    public String outOk(Long user_id, Long club_id, Model model) {
        System.out.println("user_id: " + user_id);
        System.out.println("club_id: " + club_id);
        model.addAttribute("result", clubUserListService.deleteByClubIdAndUserId(user_id, club_id));
        model.addAttribute("club_id", club_id);
        return "/club/outOk";
    }

    @PostMapping("/leave")
    public String leaveOk(Long user_id, Long club_id, Model model) {
        System.out.println("user_id: " + user_id);
        System.out.println("club_id: " + club_id);
        model.addAttribute("result", clubUserListService.deleteByClubIdAndUserId(user_id, club_id));
        model.addAttribute("club_id", club_id);
        return "/club/leaveOk";
    }


    //    @InitBinder("club") // 에러남
    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Club club = clubService.getClubById(id);
        model.addAttribute("club", club);
        return "club/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @Valid Club club
            , BindingResult result
            , @RequestParam(name = "files", required = false, defaultValue = "") MultipartFile file
            , Model model
            , RedirectAttributes redirectAttributes
    ) throws IOException {
        String imgPath = club.getRepresentative_picture();


        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            imgPath = fileName;

            try {
                Path path = Paths.get("/upload/" + imgPath);
                Files.createDirectories(path.getParent());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        club.setRepresentative_picture(imgPath);
        System.out.println("이미지경로: " + imgPath);
        System.out.println(club);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("intro", club.getIntro());
            redirectAttributes.addFlashAttribute("content", club.getContent());
            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }
            return "redirect:/club/update/" + club.getId(); // Id는 수정사항이 아니다
        }

        model.addAttribute("result", clubService.updateClub(club));
        return "club/updateOk";
    }



    @InitBinder("club")
    public void initBinder(WebDataBinder binder) {
        System.out.println("ClubController.initBinder() 호출");
        binder.setValidator(new ClubValidator());
    }

    // 클럽 글 작성
    @GetMapping("/write")
        private void clubWrite(){}

}

