package com.lec.spring.repository;

import com.lec.spring.domain.Board;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BoardRepository {

    int save(Board board);

    Board findById(Long id);

    List<Board> findByBoardTypeId(Long boardTypeId);

    int incViewCnt(Long id);

    List<Board> findAll();

    int update(Board board);

    int delete(Board board);

    // 페이징 동작 (from ~ row 까지 - 몇번째부터 몇개 select 할 것인지)
    List<Board> selectFromRow(int from, int row, @Param("param3") Long boardTypeId, @Param("param4") Long clubId);

    // 전체 글 갯수
    int countAll(Long boardTypeId, Long clubId);

    //클럽 게시판
    List<Board> findbyClubPosts(@Param("clubId")Long id,@Param("from") int from, @Param("rows") int rows, @Param("title") String title);

    //클럽 게시판 전체 개수
    int clubPostsListAll(@Param("clubId") Long id, @Param("title") String title);
}
