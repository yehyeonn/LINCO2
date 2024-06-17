//package com.lec.spring.service;
//
//import com.lec.spring.domain.User;
//import com.lec.spring.domain.UserSocializing;
//import com.lec.spring.repository.UserSocializingRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserSocializingServiceImpl implements UserSocializingService {
//
//    @Autowired
//    private UserSocializingRepository userSocializingRepository;
//
//    @Override
//    public int addUserToSocializing(User user, Long socializingId, String role) {
//        int result = 0;
//        UserSocializing userSocializing = UserSocializing.builder()
//                .user(user)
//                .socializing_id(socializingId)
//                .socialRole(role)
//                .build();
//        result = userSocializingRepository.save(userSocializing);
//        return result;
//    }
//
//    @Override
//    public List<UserSocializing> findBySocializingId(Long socializingId) {
//        return userSocializingRepository.findBySocializingId(socializingId);
//    }
//
//}
