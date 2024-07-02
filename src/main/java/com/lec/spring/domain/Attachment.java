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

    private String sourcename;
    private String filename;
    private boolean isImage;

    private Board board;
    private Club club;
    private Long board_id;
    private Long club_id;

    public void setBoard(Board board){
        this.board = board;
    }

    public void setClub(Club club){
        this.club = club;
    }

}
