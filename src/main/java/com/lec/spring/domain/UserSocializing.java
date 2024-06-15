package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSocializing {

    @ToString.Exclude
    private User user;  // 참가자 정보

    @ToString.Exclude
    @JsonIgnore
    private Long socializing_id;    // 어느 글 참여자인지 확인하기 위함

    private String socialRole;  // master, member
}
