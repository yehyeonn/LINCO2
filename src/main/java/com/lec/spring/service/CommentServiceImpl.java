package com.lec.spring.service;

import com.lec.spring.domain.*;
import com.lec.spring.repository.AttachmentRepository;
import com.lec.spring.repository.BoardRepository;
import com.lec.spring.repository.CommentRepository;
import com.lec.spring.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private UserRepository userRepository;

    private AttachmentRepository attachmentRepository;

    private BoardRepository boardRepository;

    // 생성자
    public CommentServiceImpl(SqlSession sqlSession){
        commentRepository = sqlSession.getMapper(CommentRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
        attachmentRepository = sqlSession.getMapper(AttachmentRepository.class);
        boardRepository = sqlSession.getMapper(BoardRepository.class);
    }

    @Override
    public QryCommentList list(Long boardId) {      // 특정 글의 댓글 목록
        QryCommentList list = new QryCommentList();

//        System.out.println("boardId : " + boardId);
        List<Comment> comments = commentRepository.findByBoard(boardId);
        List<Comment> filterComments = comments.stream()
                .filter(comment -> comment.getAttachment() == null || comment.getAttachment().getId() == null)
                .collect(Collectors.toList());

//        int cnt = commentRepository.selectCommentCount(boardId);

        int cnt = filterComments.size();

        list.setCount(cnt);
        list.setList(comments);
        list.setStatus("OK");

        return list;
    }

    // 댓글 작성
    @Override
    public QryResult write(Long boardId, Long userId, String content) {
        User user = userRepository.findById(userId);
        Board board = boardRepository.findById(boardId);
        System.out.println("boardId : " + board);
        System.out.println("boardId : " + boardId);

        Comment comment = Comment.builder()
                .user(user)
                .content(content)
                .board(board)
                .build();

        int cnt = commentRepository.save(comment);

        QryResult result = QryResult.builder()
                .count(cnt)
                .status("OK")
                .build();

        return result;
    }

    @Override
    public QryResult delete(Long id) {
        int cnt = commentRepository.deleteById(id);

        String status = "FAIL";

        // 삭제가 되면 1을 리턴
        if (cnt > 0) status = "OK";

        QryResult result = QryResult.builder()
                .count(cnt)
                .status(status)
                .build();

        return result;
    }
}
