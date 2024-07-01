package com.lec.spring.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {
    private Long id;
    private String name;
    private String category;
    private String detail_category;
    private String intro;
    private String content;
    private String representative_picture;

    private Board board;

    @ToString.Exclude
    @Builder.Default
    private List<Attachment> fileList = new ArrayList<>();



}
