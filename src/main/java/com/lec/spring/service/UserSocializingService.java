package com.lec.spring.service;

import com.lec.spring.domain.User;
import com.lec.spring.domain.UserSocializing;

import java.util.List;

public interface UserSocializingService {
    int addUserToSocializing(User user, Long socializingId, String role);
    List<UserSocializing> findBySocializingId(Long socializingId);
}
