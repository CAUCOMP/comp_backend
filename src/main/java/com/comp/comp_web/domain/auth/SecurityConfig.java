package com.comp.comp_web.domain.auth;

import com.comp.comp_web.domain.auth.filter.JwtAuthenticationFilter;
import com.comp.comp_web.domain.auth.handler.RestAuthenticationEntryPoint;
import com.comp.comp_web.global.constants.ApiConstants;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 설정
 * JWT 기반 인증을 위한 Stateless 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                // CORS 설정 (프론트엔드 도메인 허용)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF 비활성화 (JWT 사용으로 불필요)
                .csrf(csrf -> csrf.disable())

                // 세션 비활성화 (Stateless)
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 예외 처리 설정
                .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                )

                // 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                    // ⚠️ 순서 중요: 더 구체적인 경로가 먼저 와야 함

                    // 인증이 필요한 Auth API
                    .requestMatchers(ApiConstants.AUTH_LOGOUT_PATH).authenticated()

                    // Public 엔드포인트 (인증 불필요)
                    .requestMatchers(
                        ApiConstants.AUTH_API_PATTERN,        // 인증 관련 API (logout 제외)
                        ApiConstants.HEALTH_API_PATTERN,      // Health Check API
                        ApiConstants.HELLO_API_PATH,          // Hello API
                        ApiConstants.OPENAPI_DOCS_PATTERN,    // OpenAPI 문서
                        ApiConstants.OPENAPI_YAML_PATH,       // OpenAPI YAML
                        ApiConstants.SWAGGER_UI_PATTERN,      // Swagger UI 리소스
                        ApiConstants.SWAGGER_UI_HTML_PATH,    // Swagger UI 메인
                        ApiConstants.SWAGGER_RESOURCES_PATTERN, // Swagger 리소스
                        ApiConstants.WEBJARS_PATTERN,         // WebJars (Swagger UI 의존성)
                        ApiConstants.FAVICON_PATH,            // 파비콘
                        ApiConstants.ERROR_PATH               // 에러 페이지
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://127.0.0.1:5173",
            "https://caucomp.vercel.app"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
