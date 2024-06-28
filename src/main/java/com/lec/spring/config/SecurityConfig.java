package com.lec.spring.config;

import com.lec.spring.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Bean
//    public PasswordEncoder passwordEncoder() {           // 암호화는 되지만 복호화는 불가능
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return web -> web.ignoring().anyRequest();  // 어떠한 request 도 Security 가 무시함.
//    }

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())   // CSRF 비활성화
                // 접근 권한 설정
                // 사이트 회원:
                // 클럽 회원:
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/board/detail/**").authenticated()
                                .requestMatchers("/socializing/detail/**").authenticated()
                                .requestMatchers("/socializing/write").authenticated()
                                .requestMatchers("/club/detail/**", "/club/create/**").authenticated()
                                .requestMatchers("/reservation/write").authenticated()
                                .requestMatchers("/club/detail/**", "/club/create/**","/club/board/**" ).authenticated()
//                                .requestMatchers("/reservation/write").authenticated()
                                .requestMatchers("/board/write/**", "/board/update/**", "/board/delete").hasAnyAuthority("MEMBER", "ADMIN")
                                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                                .loginPage("/user/login")
                                .loginProcessingUrl("/user/login")
                                .successHandler(new CustomLoginSuccess("/home")) // 로그인 성공
                                .failureHandler(new CustomLoginFailure())    // 로그인 실패
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                                .logoutUrl("/user/logout")
                                .logoutSuccessUrl("/home")
                                .invalidateHttpSession(false)
                                .logoutSuccessHandler(new CustomLogoutSuccess())

                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedHandler(new CustomAccessDenied())   // 오류 발생 시
                )

                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/user/login")    // 로그인 페이지를 기존과 동일한 url 로 지정
                        // ↑ 구글 로그인 완료된 뒤에 후처리가 필요하다!

                        // code 를 받아오는 것이 아니라, 'AccessToken' 과 사용자 '프로필정보'를 한번에 받아온다
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                // 인증서버의 userinfo endpoint 설정
                                .userService(principalOauth2UserService)  // userService(OAuth2UserService)
                        )
                )



                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
