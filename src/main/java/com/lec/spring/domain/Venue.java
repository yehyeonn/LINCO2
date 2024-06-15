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
    private String venu_name;
    private String address;
    private Integer limit_num;
    private String venu_category;
    private String info_tel;
    private Long price;
    private Long total_price;
    private LocalTime open_time;
    private LocalTime close_time;
    private LocalDate reservate_date;
    private LocalTime reservate_start_time;
    private LocalTime reservate_end_time;
    private String img;
    private LocalDateTime paydate;
}
