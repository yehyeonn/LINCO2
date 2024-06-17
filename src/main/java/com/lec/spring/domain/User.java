package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
    @JsonIgnore
    private String password;    // 비밀번호

    @JsonIgnore
    @ToString.Exclude
    private String re_password;

    private String name;
    private String address;
    private String gender;
    private LocalDate birthday;
    private String profile_picture;

    @JsonIgnore
    private LocalDateTime regdate;

    // OAuth2 Client # 추가
    private String provider;
    private String providerId;
    }
