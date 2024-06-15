package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String tel;
    private String username;    // 이메일형식
    private String password;    // 비밀번호
    private String name;
    private String address;
    private String gender;
    private LocalDate birthday;
    private String profile_picture;
    private LocalDateTime regdate;

    // OAuth2 Client # 추가
    private String provider;
    private String providerId;
    }
