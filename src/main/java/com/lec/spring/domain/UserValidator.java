package com.lec.spring.domain;

import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        System.out.println("validate() 호출 : " + user);

        // 이메일 중복 검사
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "이미 가입된 이메일입니다.");
        }

        // 비밀번호 규칙 검사
        if (!user.getPassword().matches("(?=.*\\d)(?=.*[a-z]).{8,}")) {
            errors.rejectValue("password", "비밀번호는 8자리 이상, 소문자 영어와 숫자를 포함해야 합니다.");
        }

        // 비밀번호 규칙 검사
        if (!user.getTel().matches("\\d{11}")) {
            errors.rejectValue("tel", "11자리 숫자를 입력하세요");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "이름은 필수 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "비밀번호는 필수 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "이름은 필수 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tel", "전화번호는 필수 입니다.");

        // 추가적인 유효성 검사 로직을 여기에 추가
    }
}
