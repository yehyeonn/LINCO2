package com.lec.spring.repository;

import com.lec.spring.domain.Board;
import com.lec.spring.domain.Club;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ClubRepository {

    // 클럽 생성
    int save(Club club);

    // 특정 id 클럽 찾기
    Club findById(Long id);

    // 이름으로 클럽 찾기
    Club findByName(@Param("name") String name);


    // 전체 클럽 목록
    List<Club> findAll();

    // 특정 id 클럽의 상세 내용 수정(소개, 상세내용, 대표 이미지)
    int update(Club club);


    // 특정 id 클럽 삭제
    int deleteById(Long id);


    // 페이징
    // from 부터 row 개 만큼 SELECT
    List<Club> selectFromRow(int from, int rows, @Param("param3") String selectcategory, @Param("param4") String selectdetailcategory);

    // 전체 클럽의 개수
    int countAll();

    int countSelect(String category, String detailcategory);

    List<Board> findBoardByClubIdAndType(@Param("clubId") Long clubId);
}
