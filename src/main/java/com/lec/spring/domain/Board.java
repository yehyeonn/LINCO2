package com.lec.spring.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    private Long id;
    private String title;
    private String content;
    private Long viewcnt;
    private LocalDateTime regdate;

    private User user;
    private Club club;
    private BoardType boardType;

    @ToString.Exclude
    @Builder.Default
    private List<Attachment> fileList =  new ArrayList<>();
}
