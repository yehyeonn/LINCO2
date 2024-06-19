package com.lec.spring.service;

import com.lec.spring.domain.Comment;
import com.lec.spring.domain.QryCommentList;
import com.lec.spring.domain.QryResult;

import java.util.List;

public interface CommentService {
    // 특정 글(BoardId) 의 댓글 목록
    QryCommentList list(Long boardId);      // 글의 id

    // 특정 글(BoardId) 에 특정 사용자(userId) 가 댓글 작성
    QryResult write(Long boardId, Long userId, String content);

    // 특정 댓글(id) 삭제
    QryResult delete(Long id);


}
