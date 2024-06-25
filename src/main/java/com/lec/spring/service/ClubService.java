package com.lec.spring.service;

import com.lec.spring.domain.Club;
import com.lec.spring.domain.ClubUserList;
import org.springframework.ui.Model;

import java.util.List;

public interface ClubService {

    // 클럽 생성 (유저가 master 가 됨)
    int createClub(Club club);

    // 클럽 가입 (유저가 member 가 됨)
    void addMemberToClub(Long user_id, Long club_id);

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

    // 특정 클럽 조회
    Club getClubById(Long club_id);

    // 클럽 사용자 목록 조회
    List<ClubUserList> getClubUsers(Long club_id);


    // 특정 사용자의 클럽 목록 조회
    List<ClubUserList> getUserClubs(Long user_id);


}
