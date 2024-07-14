package com.example.securityjwt.jwt;

import com.example.securityjwt.dto.CustomUserDetails;
import com.example.securityjwt.entity.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // header에서 access키에 담긴 토큰을 꺼낸다
        String accessToken = request.getHeader("access");

        // 토큰이 없다면(null) 다음 필터로 넘긴다(doFilter)
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // access 토큰 만료 여부 확인. 만료시에 다음 필터로 넘기지 않는다 -> 리프레시 토큰으로 액세스 토큰 재발급 받게 한다
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            // 만료되면 예외 발생 catch 블록으로 넘어온다

            // response body에 만료 메시지를 담는다
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            // response 상태 코드 설정 - 401 -> 프론트와 협의 필요
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 'access' 토큰인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            // response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            // response 상태 코드 설정 - 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 유효한 액세스 토큰이니 일회성 세션 만들어 진행
        // username, role 값 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // authToken에 UserDetails가 필요
        // 우리가 만든 CustomUserDetails는 userEntity가 필요
        // 따라서 userEntity에 토큰에서 얻어온 유저 정보를 담는다.
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setRole(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 넘긴다
        filterChain.doFilter(request, response);
    }
}
