package com.lec.spring.repository;

import com.lec.spring.domain.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentRepository {
    // 특정 글(board_id) 의 댓글 목록
    List<Comment> findByBoard(Long board_id);

    // 댓글 작성 <-- Comment
    int save(Comment comment);

    // 특정 댓글 (id) 삭제
    int deleteById(Long id);

    int selectCommentCount(@Param("param1") Long board_id);
}
