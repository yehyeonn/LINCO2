package com.lec.spring.controller;

import com.lec.spring.domain.*;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.service.*;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/board")
public class BoardController {

//    @Value("upload")
//    private String uploadDir;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserService userService;

    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ClubUserListService clubUserListService;

    // 기본생성자
    public BoardController() {
    }

    @GetMapping("/write")
    public void write(Principal principal, @RequestParam(value = "boaredType", required = false, defaultValue = "1") int boardType, Model model) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        if (user == null){
            throw new NullPointerException("User Not Found" + username);
        }
        Long userId = user.getId();
//        User userIds = userService.findByUserId(userId);
//        System.out.println("UserId 에용 : " + userId);
//        System.out.println("userid에용 : " + userIds);
//        System.out.println("board에용 : " + board);

        List<ClubUserList> userClubs = clubUserListService.findByUserId(userId);
//         디버깅용
//        for(ClubUserList userClub : userClubs){
//            Club userClubEntity = userClub.getClub();
//            if (userClubEntity != null){
//                System.out.println("Club name : " + userClubEntity.getName());
//            }
//        }
        model.addAttribute("boardType", boardType);
        model.addAttribute("userclubsSize", userClubs.size());
        model.addAttribute("userclubs", userClubs);
    }

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files,
            @RequestParam("boardType.id") int boardTypeId,
            @RequestParam("clubId") Optional<Long> clubId,
            @Valid Board board,
            BindingResult boardResult,
            @Valid ClubUserList clubUserList,
            BindingResult clubUserListResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // boardType 이 공지사항 이거나 자유게시판일 경우
        if (boardTypeId == 2 || boardTypeId == 1) {
            if (boardResult.hasErrors()) {
                handleErrors(boardResult, board, redirectAttributes);
                return "redirect:/board/write?boardType=" + boardTypeId;
            }
            int result = boardService.write(board, files);

            model.addAttribute("result", result);
        // boardType 이 클럽홍보 일 경우
        } else if (boardTypeId == 3) {
            if (clubUserListResult.hasErrors()) {
                handleClubUserListErrors(clubUserListResult, clubUserList, redirectAttributes);
                return "redirect:/board/write?boardType=" + boardTypeId;
            }
            Club club = clubService.getClubById(clubId.get());

            board.setClub(club);

            int result = boardService.write(board, files);

            model.addAttribute("result", result);
        }
        return "board/writeOk";
    }

    private void handleErrors(BindingResult result, Board board, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("board", board);
        for(FieldError err : result.getFieldErrors()){
            redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
        }
    }

    private void handleClubUserListErrors(BindingResult result, ClubUserList clubUserList, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("clubUserList", clubUserList);
        for(FieldError err : result.getFieldErrors()){
            redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
        }
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Board board = boardService.detail(id);
        model.addAttribute("board", board);
//        디버깅 용도
//        System.out.println("board : " +board.toString());

        Club club = board.getClub() != null ? clubService.getClubById(board.getClub().getId()) : null;
        model.addAttribute("club", club);

        String content = board.getContent().replace("\n", "<br>");

        List<Comment> comments = commentService.list(id).getList();  // 게시물의 댓글 목록을 가져옴

        List<Comment> filterComments = comments.stream()
                .filter(comment -> comment.getAttachment().getId() == null || comment.getAttachment().getId() == null)
                .collect(Collectors.toList());
        int cnt = commentService.list(id).getCount();

        List<Attachment> attachments = attachmentService.findByAttachment(id);

        model.addAttribute("attachments", attachments);
        model.addAttribute("content", content);
        model.addAttribute("cnt", cnt);
        model.addAttribute("comments", comments);

//        디버깅 용도
//        System.out.println("filter comments 갯수 : " + filterComments.size());
//        comments.forEach(comment -> {
//            System.out.println("comment id : " + comment.getId());
//            System.out.println("comment content : " + comment.getContent());
//            System.out.println("comment attachment id : " + (comment.getAttachment() != null ? comment.getAttachment().getId() : null));
//        });

        return "board/detail";
    }

    @GetMapping("/list")
    public String list(Integer page, Model model, @RequestParam(name = "boardTypeId", required = false, defaultValue = "2") Long boardTypeId, @RequestParam(name = "clubId", required = false, defaultValue = "") Long clubId) {
//        boardService.list(page, model);

        List<Board> boards = boardService.list(page, model, boardTypeId, clubId);
        List<Club> clubs = clubService.getAllClubs();
//        Attachment attachment = attachmentService.findByAttachment();

        model.addAttribute("boards", boards);
        model.addAttribute("clubs", clubs);

//        System.out.println("boards : " + boards);
        return "board/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        List<Attachment> attachments = attachmentService.findByAttachment(id);

        model.addAttribute("attachments", attachments);
        model.addAttribute("board", boardService.findById(id));
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam Map<String, MultipartFile> files,
            @Valid Board board,
            BindingResult result,
            Long[] delfile,
            Model model,
            RedirectAttributes redirectAttrs
    ) {
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("title", board.getTitle());
            redirectAttrs.addFlashAttribute("content", board.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }
//            System.out.println("ErrList" + errList);
            return "redirect:/board/update/" + board.getId();
        }

        int updateResult = boardService.update(board, files, delfile);
        model.addAttribute("result", updateResult);

//        System.out.println("Update result : " + updateResult);
        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model) {
        model.addAttribute("result", boardService.deleteById(id));

        return "board/deleteOk";
    }

    @InitBinder("board")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new BoardValidator());
    }

    @InitBinder("clubUserList")
    public void initClubUserListBinder(WebDataBinder binder) {
        binder.setValidator(new ClubUserListValidator());
    }

    // 페이징 관련
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows) {
        U.getSession().setAttribute("pageRows", pageRows);

        return "redirect:/board/list?page=" + page;
    }

}
