package com.lec.spring.controller;

import com.lec.spring.domain.*;
import com.lec.spring.service.*;
import com.lec.spring.util.U;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserService userService;

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
    public void write() {

    }

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files,
            @RequestParam("boardType.id") int boardTypeId,
            @Valid Board board,
            BindingResult boardResult,          // result -> boardResult
            @Valid ClubUserList clubUserList,
            BindingResult clubUserListResult,
            User user,
            Club club,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // boardType 이 공지사항 이거나 자유게시판일 경우
        if (boardTypeId == 2) {
            if (boardResult.hasErrors()) {
                handleErrors(boardResult, board, redirectAttributes);
                return "redirect:/board/write";
            }
            int result = boardService.write(board, files);

            model.addAttribute("result", result);
//            System.out.println("result2 : " + result);
        } else if (boardTypeId == 1) {
            if (boardResult.hasErrors()) {
                handleErrors(boardResult, board, redirectAttributes);
            }
            int result = boardService.write(board, files);

            model.addAttribute("result", result);
//            System.out.println("result1 : " + result);

        } else if (boardTypeId == 3) { // boardType 이 클럽홍보 일 경우
            if (clubUserListResult.hasErrors()) {
                handleErrors(clubUserListResult, clubUserList, redirectAttributes);
            }
            int result = boardService.write(board, files);
            System.out.println("userID : " + user.getId());
            List<ClubUserList> resultI = clubUserListService.findByUserId(user.getId());
            for (ClubUserList e : resultI) {
                System.out.println("result i :" + e);
            }
            model.addAttribute("result", result);
        }

        return "board/writeOk";
    }

    private void handleErrors(BindingResult result, Object target, RedirectAttributes redirectAttributes) {
        if (target instanceof Board) {
            Board board = (Board) target;
            redirectAttributes.addFlashAttribute("user", board.getUser());
            redirectAttributes.addFlashAttribute("title", board.getTitle());
            redirectAttributes.addFlashAttribute("content", board.getContent());
//        }else if(target instanceof ClubUserList){
//            ClubUserList clubUserList = (ClubUserList) target;
//            redirectAttributes.addFlashAttribute("user", clubUserList.)
        }
        List<FieldError> errList = result.getFieldErrors();
        for (FieldError err : errList) {
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

        List<Comment> comments = commentService.list(id).getList();  // 게시물의 댓글 목록을 가져옴

        List<Comment> filterComments = comments.stream()
                .filter(comment -> comment.getAttachment().getId() == null || comment.getAttachment().getId() == null)
                .collect(Collectors.toList());
        int cnt = commentService.list(id).getCount();

        List<Attachment> attachments = attachmentService.findByAttachment(id);

        model.addAttribute("attachments", attachments);
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
    public String list(Integer page, Model model, @RequestParam(name = "boardTypeId", required = false, defaultValue = "2") Long boardTypeId, @RequestParam(name = "clubId", required = false, defaultValue = "") Long clubId) {      // @RequestParam(required = false, defaultValue = "1") Integer page
//        boardService.list(page, model);

        List<Board> boards = boardService.list(page, model, boardTypeId, clubId);
        List<Club> clubs = clubService.getAllClubs();

        model.addAttribute("boards", boards);
        model.addAttribute("clubs", clubs);

        // 디버깅용
//        for (int i = 1; i < boards.size(); i++) {
//            System.out.println("boards : " +boards.get(i).getClub().toString() + "\n");
//        }
//        System.out.println("boardType : " +boardTypeId + "\n");

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
            System.out.println("ErrList" + errList);
            return "redirect:/board/update/" + board.getId();
        }

        int updateResult = boardService.update(board, files, delfile);
        model.addAttribute("result", updateResult);

        System.out.println("Update result : " + updateResult);
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

    // 페이징 관련
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows) {
        U.getSession().setAttribute("pageRows", pageRows);

        return "redirect:/board/list?page=" + page;
    }

}
