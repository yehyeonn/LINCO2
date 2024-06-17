package com.lec.spring.service;

import com.lec.spring.domain.User;
import com.lec.spring.domain.UserSocializing;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.repository.UserSocializingRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSocializingServiceImpl implements UserSocializingService {

    private UserRepository userRepository;
    private UserSocializingRepository userSocializingRepository;

    @Autowired
    public UserSocializingServiceImpl(SqlSession sqlSession) {
        userRepository = sqlSession.getMapper(UserRepository.class);
        userSocializingRepository = sqlSession.getMapper(UserSocializingRepository.class);
    }


    @Override
    public int addUserToSocializing(Long userId, Long socializingId, String role) {
        User user = userRepository.findById(userId);
        int result = 0;
        result = userSocializingRepository.addUserToSocializing(userId, socializingId, role);
        return 0;
    }

    @Override
    public List<UserSocializing> findBySocializingId(Long socializingId) {
        return List.of();
    }
}
