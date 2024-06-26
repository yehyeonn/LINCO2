package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    private Long id;
    private LocalDate reserve_date;
    private LocalTime reserve_start_time;
    private LocalTime reserve_end_time;
    private String status;
    private Venue venue;
    private User user;
}
