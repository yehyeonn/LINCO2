package com.lec.spring.service;

import com.lec.spring.domain.Socializing;
import com.lec.spring.domain.UserSocializing;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface SocializingService {
    // 작성, 내용보기, 전체 목록, 수정하기, 삭제
    int write(Socializing socializing);

    @Transactional
    Socializing detail(Long id);

    int membercnt(Long id);

    List<UserSocializing> socializingMemberList(Long id);


    List<Socializing> list(Integer page
            , Model model
            , String selectaddress
            , String selectcategory
            , String selectdetailcategory
    );

    UserSocializing findBySocializingMaster(long id);

    // 수정
    Socializing selectById(Long id, Model model);
    // 제목, 내용
    int update(Socializing socializing);

    List<String> getAllCategories();


    // 삭제
    int deleteById(Long id);

    // 첨부파일
    // TODO
}
