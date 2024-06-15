package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
