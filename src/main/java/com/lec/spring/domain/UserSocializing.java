package com.lec.spring.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSocializing {
    private User user;  // 참가자 정보
    private Socializing socializing;    // 어느 글 참여자인지 확인하기 위함
    private String role;  // master, member

}
