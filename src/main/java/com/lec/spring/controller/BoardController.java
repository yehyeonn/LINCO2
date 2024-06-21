package com.lec.spring.controller;

import com.lec.spring.domain.*;
import com.lec.spring.service.BoardService;
import com.lec.spring.service.ClubService;
import com.lec.spring.service.CommentService;
import com.lec.spring.service.UserService;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
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

    // 기본생성자
    public BoardController() {
    }

    @GetMapping("/write")
    public void write(){

    }

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files,
            @Valid Board board,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("user", board.getUser());
            redirectAttributes.addFlashAttribute("title", board.getTitle());
            redirectAttributes.addFlashAttribute("content", board.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList){
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }
            return "redirect:/board/write";
        }
        model.addAttribute("result", boardService.write(board, files));

        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        Board board = boardService.detail(id);
        model.addAttribute("board", board);
//        System.out.println("board : " +board.toString());

        Club club = board.getClub() != null ? clubService.getClubById(board.getClub().getId()) : null;
        model.addAttribute("club", club);

        List<Comment> comments = commentService.list(id).getList();  // 게시물의 댓글 목록을 가져옴

        List<Comment> filterComments = comments.stream()
                .filter(comment -> comment.getAttachment().getId() == null ||comment.getAttachment().getId() == null)
                .collect(Collectors.toList());
        int cnt = commentService.list(id).getCount();

        model.addAttribute("cnt", cnt);
        model.addAttribute("comments", comments);

//        System.out.println("filter comments 갯수 : " + filterComments.size());
//        comments.forEach(comment -> {
//            System.out.println("comment id : " + comment.getId());
//            System.out.println("comment content : " + comment.getContent());
//            System.out.println("comment attachment id : " + (comment.getAttachment() != null ? comment.getAttachment().getId() : null));
//        });

        return "board/detail";
    }

    @GetMapping("/list")
    public String list(Integer page, Model model, @RequestParam(name = "boardTypeId", required = false, defaultValue = "2") Long boardTypeId, @RequestParam(name = "clubId", required = false, defaultValue = "") Long clubId){      // @RequestParam(required = false, defaultValue = "1") Integer page
//        boardService.list(page, model);

        List<Board> boards = boardService.list(page, model, boardTypeId, clubId);
        List<Club> clubs = clubService.getAllClubs();

        model.addAttribute("boards", boards);
        model.addAttribute("clubs", clubs);
//        System.out.println("boards : " +boards.toString() + "\n");
//        System.out.println("boardType : " +boardTypeId + "\n");

        return "board/list1";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("board", boardService.findById(id));
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam Map<String, MultipartFile> files,
            @Valid Board board,
            BindingResult result,
            Long [] delfile,
            Model model,
            RedirectAttributes redirectAttrs
    ){
        if (result.hasErrors()){
            redirectAttrs.addFlashAttribute("title", board.getTitle());
            redirectAttrs.addFlashAttribute("content", board.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }
            return "redirect:/common/board_update" + board.getId();
        }
        model.addAttribute("result", boardService.update(board, files, delfile));

        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){
        model.addAttribute("result", boardService.deleteById(id));

        return "board/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(new BoardValidator());
    }

    // 페이징 관련
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows){
        U.getSession().setAttribute("pageRows", pageRows);

        return "redirect:/board/list?page=" + page;
    }

}
