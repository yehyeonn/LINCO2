package com.lec.spring.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {
    private Long id;
    private String venue_name;
    private String address;
    private Integer limit_num;
    private String venue_category;
    private String info_tel;
    private Long price;
    private String posible_start_date;
    private String posible_end_date;
    private String open_time;
    private String close_time;
    private String img;
}
