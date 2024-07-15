package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 단방향 해시 암호화를 위한 BCryptPasswordEncoder를 빈으로 등록
    /*PasswordEncoder를 BCryptPasswordEncoder로 빈 등록했으므로
    검증 과정에서도 자동으로 BCryptPasswordEncoder 사용*/
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // 접근 권한 설정 (인증, 인가)
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

        // 커스텀 로그인 설정
        // /loginProc post 요청 처리 부분은 필터가 받아서 처리한다 -> 따라서 컨트롤러는 필요하지 않다
        // 정확히는 UsernamePasswordAuthenticationFilter
        http
                .formLogin((auth) -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll());

        /*post 요청 보낼 때 csrf 토큰도 보내 주어야 로그인이 진행된다
        일단 disable 시키고 개발 진행*/
        http
                .csrf((auth) -> auth.disable());

        // 빌더 타입으로 반환
        return http.build();
    }

}
