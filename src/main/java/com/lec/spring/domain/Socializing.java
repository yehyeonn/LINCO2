package com.lec.spring.domain;

import lombok.*;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Socializing {
    private Long id;
    private String socializing_title;
    private String category;
    private String detail_category;
    private String address;
    private String place_name;
    private LocalDate meeting_date;
    private LocalTime meeting_time;
    private Integer limit_num;
    private String content;
    private Long total_price;
    private String img;
    private LocalDateTime regdate;
    private Long venue_id;
    private Venue venue;

    private User user;

}
