package com.lec.spring.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class BoardValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        boolean result = Board.class.isAssignableFrom(clazz);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Board board = (Board) target;
        System.out.println("BoardValidator 호출" + board);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"title", "글 제목은 필수입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"content", "내용은 필수입니다.");
    }
}
