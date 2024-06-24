package com.lec.spring.util;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class U {

    // 현재 로그인 한 사용자 정보
    public static User getLoggedUser() {
        PrincipalDetails userDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return user;
    }

    // 현재 request 구하기
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attrs.getRequest();
    }

    // 현재 session 구하기
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static void printFileInfo(MultipartFile file){
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()){
            System.out.println("\t 파일 없음");
            return;
        }
        System.out.println("""
                Original File Name : %s,
                ClearPath : %s,
                File Size : %s,
                MIME : %s
                """.formatted(originalFilename, StringUtils.cleanPath(originalFilename), file.getSize() + "bytes", file.getContentType()));
    }
}
