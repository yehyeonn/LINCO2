package com.lec.spring.config;

import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {

    @Setter
    private UserService userService;

    @Getter
    private User user;

    // 일반 로그인 생성자
    public PrincipalDetails(User user) { // 에러
        System.out.println("UserDetails(user) 생성: " + user);
        this.user = user;
    }

    // OAuth2 로그인 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes){
        // 확인용 출력코드
        System.out.println("""
       UserDetails(user, oauth attributes) 생성:
           user: %s
           attributes: %s
       """.formatted(user, attributes));

        this.user = user;
        this.attributes = attributes;
    }

    // return 값 바꿔야함
    @Override
    public String getName() {
        return "";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


    //----------------------------------------------------
    // OAuth2User 를 implement 하게 되면 구현할 메소드

    private Map<String, Object> attributes;  // OAuth2User 의 getAttributes() 값

    // TODO

}
