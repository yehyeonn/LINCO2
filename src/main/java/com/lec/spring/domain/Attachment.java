package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    private Long id;

    private String sourceName;
    private String fileName;
    private boolean isImage;

    private Board board;
    private Club club;

    public void setBoard(Board board){
        this.board = board;
    }
}
