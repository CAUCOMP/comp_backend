package com.comp.comp_web.domain.auth;

import com.comp.comp_web.domain.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정
 * JWT 기반 인증을 위한 Stateless 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                // CSRF 비활성화 (JWT 사용으로 불필요)
                .csrf(csrf -> csrf.disable())

                // 세션 비활성화 (Stateless)
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                    // Public 엔드포인트 (인증 불필요)
                    .requestMatchers(
                        "/api/auth/**",              // 인증 관련 API
                        "/v3/api-docs/**",           // OpenAPI 문서
                        "/v3/api-docs.yaml",         // OpenAPI YAML
                        "/swagger-ui/**",            // Swagger UI 리소스
                        "/swagger-ui.html",          // Swagger UI 메인
                        "/swagger-resources/**",     // Swagger 리소스
                        "/webjars/**",               // WebJars (Swagger UI 의존성)
                        "/favicon.ico",              // 파비콘
                        "/error"                     // 에러 페이지
                    ).permitAll()

                    // 그 외 모든 요청은 인증 필요 (Bearer Token)
                    .anyRequest().authenticated()
                )

                // JWT 인증 필터 추가
                .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
                );

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Security 설정 중 오류 발생", e);
        }
    }

    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
