package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomLoginSuccess extends SavedRequestAwareAuthenticationSuccessHandler {
    public CustomLoginSuccess(String defaultUrl) {
        setDefaultTargetUrl(defaultUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        System.out.println("# 로그인 테스트");
        System.out.println("ip" + getClientIp(request));
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();    // UserDetail 보기
        System.out.println(userDetails.getUser());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.getPassword());
        List<String> role = new ArrayList<String>();
        authentication.getAuthorities().forEach(authority -> {
            role.add(authority.getAuthority());
        });
        System.out.println(role);

        super.onAuthenticationSuccess(request, response, authentication);   // 로그인 전 url 로 redirect
    }



    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
