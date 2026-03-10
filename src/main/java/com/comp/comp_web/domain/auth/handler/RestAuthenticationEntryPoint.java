package com.comp.comp_web.domain.auth.handler;

import com.comp.comp_web.global.response.ApiResponse;
import com.comp.comp_web.global.response.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 인증 실패 시 일관된 401 JSON 응답을 반환하는 진입점
 * Spring Security에서 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출됩니다.
 */
@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        // Java 8 날짜/시간 모듈 등록
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        log.warn("인증 실패: {} - {}", request.getRequestURI(), authException.getMessage());

        // 응답 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 에러 응답 생성
        ApiResponse<Void> errorResponse = ApiResponse.error(
            ErrorCode.AUTH_003,  // "인증이 필요합니다."
            "유효한 인증 토큰을 제공해주세요."
        );

        // JSON으로 변환하여 응답
        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
