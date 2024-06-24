package com.lec.spring.repository;

import com.lec.spring.domain.Socializing;
import com.lec.spring.domain.UserSocializing;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SocializingTest {
    @Autowired
    private SqlSession sqlSession;

    @Test
    void test1(){
        SocializingRepository socializingRepository = sqlSession.getMapper(SocializingRepository.class);

        List<UserSocializing> userSocializing =  socializingRepository.findBySocialMembers(1L);
        for (int i = 0; i < userSocializing.size(); i++) {
            System.out.println("아 좀 나와라~~~~~~~~~~~"+userSocializing.toArray()[i]);
            System.out.println();

        }


    }
}
