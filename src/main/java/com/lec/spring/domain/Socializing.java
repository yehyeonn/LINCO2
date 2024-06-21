package com.lec.spring.domain;

import lombok.*;

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
    private LocalDate meeting_date;
    private LocalTime meeting_time;
    private Integer limit_num;
    private String content;
    private Long total_price;
    private String img;
    private LocalDateTime regdate;

    private Venue venue;

    private User user;
    public Long getVenue_id() {
        return venue != null ? venue.getId() : null;
    }

    public void setVenue_id(Long venue_id) {
        if (venue == null) {
            venue = new Venue();
        }
        venue.setId(venue_id);
    }
}
