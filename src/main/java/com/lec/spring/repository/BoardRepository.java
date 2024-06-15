package com.lec.spring.repository;

import com.lec.spring.domain.Board;

import java.util.List;

public interface BoardRepository {

    int save(Board board);

    Board findById(Long id);

    Board findByBoardTypeId(Long boardTypeId);

    int incViewCnt(Long id);

    List<Board> findAll();

    int update(Board board);

    int delete(Board board);

    // 페이징 동작 (from ~ row 까지 - 몇번째부터 몇개 select 할 것인지)
    List<Board> selectFromRow(int from, int row);

    // 전체 글 갯수
    int countAll();
}
