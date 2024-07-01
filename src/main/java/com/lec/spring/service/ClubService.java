package com.lec.spring.service;

import com.lec.spring.domain.Club;
import com.lec.spring.domain.ClubUserList;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

public interface ClubService {

    // 클럽 생성 (유저가 master 가 됨)
    int createClub(Club club);

    // 클럽 삭제
    int deleteById(Long club_id);

    // 클럽 가입 (유저가 member 가 됨)
    int addMemberToClub(Long user_id, Long club_id);

    // 클럽 사용자 삭제 (MASTER만 가능)
    void deleteClubMember(Long user_id, Long club_id);

    // 클럽 정보 수정
    int updateClub(Club club);


    // 클럽 목록 조회
    List<Club> getAllClubs();

    List<Club> list(Integer page,
                    Model model,
                    String selectcategory,
                    String selectdetailcategory);

    @Transactional
    Club detail(Long id);

    // 클럽장 조회
    ClubUserList findClubMaster(Long club_id);

    // 특정 클럽 조회
    Club getClubById(Long club_id);



    // 클럽 인원수 조회
    int getClubMemberCount(Long club_id);


    // 클럽 이름 중복확인
    boolean isClubNameExists(String clubName);
}
