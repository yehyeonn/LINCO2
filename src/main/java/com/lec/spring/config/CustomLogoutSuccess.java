package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccess implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("로그아웃 성공 테스트");

        String redirectUrl = "/user/login?logoutHandler";

        if(request.getParameter("ret_url") != null){
            redirectUrl = request.getParameter("ret_url");
        }
        response.sendRedirect(redirectUrl);
    }
}
