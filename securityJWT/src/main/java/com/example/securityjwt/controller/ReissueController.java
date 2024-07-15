package com.example.securityjwt.controller;

import com.example.securityjwt.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class ReissueController {

    private final JWTUtil jwtUtil;

    public ReissueController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 추후 서비스 로직 분리할 것!
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // 리프레시 토큰 얻기
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        // 리프레시 토큰이 없는 경우(null) - 400 응답 코드
        if (refreshToken == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 리프레시 토큰 만료 체크
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            // 만료시 예외 발생
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 'refresh' 토큰인지 확인
        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {
            // refresh 토큰이 아닌 경우
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 토큰 만들기 과정
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 새로운 액세스 토큰을 만들어 준다
        String newAccessToken = jwtUtil.createJwt("access", username, role, 600000L);

        // 새로운 리프레시 토큰을 만들어준다
        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // 응답 헤더 중 "Authorization" 헤더에 새 액세스 토큰을 담는다
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        // 쿠키에 새 리프레시 토큰을 담아보낸다
        response.addCookie(createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}