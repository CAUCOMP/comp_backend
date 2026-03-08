package com.comp.comp_web.domain.auth.filter;

import com.comp.comp_web.domain.auth.entity.Role;
import com.comp.comp_web.domain.auth.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 인증 필터
 * 모든 요청의 Authorization 헤더에서 JWT 토큰을 추출하고 검증합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Authorization 헤더에서 JWT 추출
            String token = resolveToken(request);

            // 2. 토큰 검증
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                // 3. Authentication 객체 생성
                Authentication authentication = getAuthentication(token);

                // 4. SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT 인증 성공: userId={}", jwtTokenProvider.getUserId(token));
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생", e);
            // 예외가 발생해도 필터 체인은 계속 진행 (인증 실패로 처리)
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     * @param request HTTP 요청
     * @return JWT 토큰 (없으면 null)
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * JWT 토큰에서 Authentication 객체 생성
     * @param token JWT 토큰
     * @return Authentication 객체
     */
    private Authentication getAuthentication(String token) {
        Long userId = jwtTokenProvider.getUserId(token);
        String email = jwtTokenProvider.getEmail(token);
        Role role = jwtTokenProvider.getRole(token);

        // Spring Security의 GrantedAuthority 생성
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());

        // UsernamePasswordAuthenticationToken 생성
        // principal: userId, credentials: null (이미 인증됨), authorities: 권한 목록
        return new UsernamePasswordAuthenticationToken(
            userId,
            null,
            Collections.singletonList(authority)
        );
    }
}
