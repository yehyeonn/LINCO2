package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PublicReservationDTO {

    @JsonProperty("ListPublicReservationInstitution")
    ListPublicReservationInstitution listPublicReservationInstitution;

    @Data
    public static class ListPublicReservationInstitution {
        public int list_total_count;

        List<Reservation> row;
    }


    @Data
    public static class Reservation{
        @JsonProperty("GUBUN")
        public String gubun;

        @JsonProperty("SVCID")
        public String svcId;


    }
}
