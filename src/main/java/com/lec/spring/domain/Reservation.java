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
    private Venue venue;
    private User user;
    private LocalDate reservate_date;
    private LocalTime reservate_start_time;
    private LocalTime reservate_end_time;
    private String status;
}
