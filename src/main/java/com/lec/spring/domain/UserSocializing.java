package com.lec.spring.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSocializing {
    private User user;
    private Socializing socializing;
    private Long user_id; // 참가자 id
    private Long socializing_id; // 참여글 아이디
    private String role;  // master, member

}
