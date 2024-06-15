package com.lec.spring.controller;

import com.lec.spring.domain.Board;
import com.lec.spring.service.BoardService;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

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
            return "redirect:/common/board_write";
        }
        model.addAttribute("result", boardService.write(board, files));

        return "common/board_writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        model.addAttribute("board", boardService.detail(id));

        return "common/board_detail";
    }

    @GetMapping("/list")
    public void list(Integer page, Model model){
        boardService.list(page, model);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("board", boardService.selectById(id));
        return "common/board_update";
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

        return "common/board_updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){
        model.addAttribute("result", boardService.deleteById(id));

        return "common/board_deleteOk";
    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder){
//        binder.setValidator(new BoardValidator());
//    }

    // 페이징 관련
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows){
        U.getSession().setAttribute("pageRows", pageRows);

        return "redirect:/common/board_list?page=" + page;
    }

}
