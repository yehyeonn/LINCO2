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
public class Reservation {

    private Long id;
    private Long user_id;
    private String reservation_name;
    private String email;
    private String tel;
    private String status;
    private LocalDate reserve_date;
    private LocalTime reserve_start_time;
    private LocalTime reserve_end_time;
    private Long total_price;
    private LocalDateTime paydate;
    private Venue venue;
    private User user;
}
