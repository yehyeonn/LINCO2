package com.lec.spring.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class SocializingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        boolean result = Socializing.class.isAssignableFrom(clazz);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Socializing socializing = (Socializing) target;
//        System.out.println("validate() 호출 : " + socializing);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "socializing_title", "글 제목은 필수 입력입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "detail_category", "카테고리 선택은 필수입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "meeting_date", "날짜와 시간은 필수 입력입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "meeting_time", "날짜와 시간은 필수 입력입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "limit_num", "인원입력은 필수입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "위치 입력은 필수입니다. 검색할 키워드를 입력해주세요");

    }
}
