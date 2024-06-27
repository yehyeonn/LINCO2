package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubUserList {
    private Long user_id;
    private Long club_id;
    private String role;
    private LocalDateTime regdate;
    private Club club;
    private User user;

    private BoardType board_type;
    private String title;
    private String content;
    private Attachment attachment;
}
