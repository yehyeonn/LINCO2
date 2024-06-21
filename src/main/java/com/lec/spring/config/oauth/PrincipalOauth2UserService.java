package com.lec.spring.config.oauth;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.config.oauth.provider.GoogleUserInfo;
import com.lec.spring.config.oauth.provider.KakaoUserInfo;
import com.lec.spring.config.oauth.provider.NaverUserInfo;
import com.lec.spring.config.oauth.provider.OAuth2UserInfo;
import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {


    @Autowired
    private UserService userService; // 회원조회, 가입

    @Value("1234")
    private String oauth2Password; // Oauth2 회원 가입시 기본 비번

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // 사용자 프로필 정보 가져오기

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = switch (provider.toLowerCase()) {
            case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            case "naver" -> new NaverUserInfo(oAuth2User.getAttributes());
            case "kakao" -> new KakaoUserInfo(oAuth2User.getAttributes());
            default -> null;
        };

        String providerId = oAuth2UserInfo.getProviderId();
        String password = oauth2Password;
        String username;

        // 카카오의 경우, username을 provider + providerId로 구성
        if (provider.equalsIgnoreCase("kakao")) {
            username = provider + "_" +  oAuth2UserInfo.getName();
        } else {
            username = oAuth2UserInfo.getEmail();
        }

        String name = oAuth2UserInfo.getName();

        // 회원가입 처리 로직 (기존 코드와 동일)
        User user = userService.findByUsername(username);
        if (user == null) {
            User newUser = User.builder()
                    .username(username)
                    .name(name)
                    .password(password)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            int cnt = userService.register(newUser);
            if (cnt > 0) {
                System.out.println("[OAuth2 인증. 회원 가입 성공]");
                user = userService.findByUsername(username);
            } else {
                System.out.println("[OAuth2 인증. 회원 가입 실패]");
            }
        } else {
            System.out.println("[OAuth2 인증. 이미 가입된 회원입니다]");
        }

        PrincipalDetails principalDetails = new PrincipalDetails(user, oAuth2User.getAttributes());
        principalDetails.setUserService(userService);

        return principalDetails;
    }
}
