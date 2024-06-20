package com.lec.spring.controller;

import com.lec.spring.domain.QryCommentList;
import com.lec.spring.domain.QryResult;
import com.lec.spring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list/{boardId}")
    public QryCommentList list(@PathVariable Long boardId) {
        QryCommentList result = commentService.list(boardId);

        // 디버깅 용도
        System.out.println("boardId : " + boardId);
        result.getList().forEach(comment -> {
            System.out.println("CommentId : " + comment.getId());
            System.out.println("Content : " + comment.getContent());
            System.out.println("User : " + comment.getUser().getUsername());
        });
        return result;
//        return commentService.list(boardId);
    }

    @PostMapping("/write")
    public QryResult write(
            @RequestParam("board_id") Long boardId,
            @RequestParam("user_id") Long userId,
            @RequestParam("content") String content
    ){
        // 디버깅 용도
        System.out.println("Writing comment for board ID: " + boardId);
        System.out.println("User ID: " + userId);
        System.out.println("Content: " + content);

        return commentService.write(boardId, userId, content);
    }

    @PostMapping("/delete")
    public QryResult delete(Long id){
        System.out.println("Deleting comment ID : " + id);

        return commentService.delete(id);
    }
}
