package com.lec.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //    @Bean
//    public PasswordEncoder encoder() {           // 암호화는 되지만 복호화는 불가능
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().anyRequest();  // 어떠한 request 도 Security 가 무시함.
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())   // CSRF 비활성화
                // 접근 권한 설정
                // 사이트 회원:
                // 클럽 회원:
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/board/detail/**").authenticated()
                                .requestMatchers("/board/write/**", "/board/update/**", "/board/delete").hasAnyRole("MEMBER", "ADMIN")
                                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .successHandler(new CustomLoginSuccess("/home")) // 로그인 성공
                                .failureHandler(new CustomLoginFailure())    // 로그인 실패
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/home")
                                .invalidateHttpSession(false)
                                .logoutSuccessHandler(new CustomLogoutSuccess())

                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedHandler(new CustomAccessDenied())   // 오류 발생 시
                )

                // oauth 부분
                // TODO


                .build();
    }
}
