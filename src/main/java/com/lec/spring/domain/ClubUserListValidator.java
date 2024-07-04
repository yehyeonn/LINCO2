package com.lec.spring.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ClubUserListValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        boolean result = ClubUserList.class.isAssignableFrom(clazz);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ClubUserList userList = (ClubUserList) target;
        System.out.println("ClubUserListValidator() 호출 : " + userList);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "Title", "글 제목은 필수 입력입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "Content", "내용은 필수 입력입니다.");
    }
}
