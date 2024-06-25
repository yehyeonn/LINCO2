package com.lec.spring.repository;

import com.lec.spring.domain.UserSocializing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserSocializingRepository {

    // 특정 글의 유저 목록
    List<UserSocializing> findBySocializingId(Long socializing_id);

    // 참가 버튼 눌렀을 때 저장되는 유저
    int addUserToSocializing(@Param("userId") Long userId
            ,@Param("socializingId") Long socializingId
            ,@Param("role") String role);

}
