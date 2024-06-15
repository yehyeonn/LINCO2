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
        System.out.println("validate() 호출 : " + socializing);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "socializing_title", "글 제목은 필수 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category", "카테고리1은 필수 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "detail_category", "카테고리2는 필수 입니다.");

        //TODO 에러 처리
    }
}
