package com.lec.spring.domain;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ClubValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return Club.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Club club = (Club) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "클럽 이름은 필수 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category", "클럽 카테고리 선택은 필수 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "intro", "클럽 소개는 필수 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "클럽 상세내용은 필수 입니다.");

        // 추가적인 유효성 검사 로직을 여기에 추가
    }
}
