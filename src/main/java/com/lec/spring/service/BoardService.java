package com.lec.spring.service;

import com.lec.spring.domain.Board;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardService {

    // 글 쓰기
    int write(Board board, Map<String, MultipartFile> files);

    // 특정 id 로 글 조회 (조회수 증가, 글 읽어오기)
    @Transactional
    Board detail(Long id);

    // 글 목록
    List<Board> list();

    // 페이징 리스트 메소드
    List<Board> list(Integer page, Model model);

    // 특정 id로 글 읽기
    Board selectById(Long id);

    // boardType 으로 글 읽기
    List<Board> findByBoardTypeId(Long boardTypeId);

    // 특정 id 글 수정 (제목, 내용, 첨부파일)
    int update(Board board, Map<String, MultipartFile> files, Long [] delfile);

    // 삭제
    int deleteById(Long id);
}
