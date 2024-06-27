package com.lec.spring.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ReservationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        boolean result = Reservation.class.isAssignableFrom(clazz);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reservation reservation = (Reservation) target;
        System.out.println("reservationValidate()를 불러볼까아~~? : " + reservation);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reservation_name" , "예약자명을 입력해 주세요.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "이메일을 입력해 주세요.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tel", "전화번호를 입력해주세요.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reserve_start_time", "예약 시간을 선택해주세요.");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reserve_end_time", "required", "예약 시간을 선택해주세요.");

    }
}
