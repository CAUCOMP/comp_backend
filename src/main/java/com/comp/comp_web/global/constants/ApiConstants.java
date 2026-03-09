package com.comp.comp_web.global.constants;

/**
 * API 관련 상수를 정의하는 클래스
 * OpenAPI(Swagger) 설정 및 보안 관련 상수를 포함합니다.
 */
public final class ApiConstants {

    // ========== OpenAPI 설정 ==========

    /**
     * API 문서 제목
     */
    public static final String API_TITLE = "COMP Backend API";

    /**
     * API 문서 설명
     */
    public static final String API_DESCRIPTION = "University Club Community Website API Documentation";

    /**
     * API 버전
     */
    public static final String API_VERSION = "v1.0.0";

    /**
     * API 담당 팀 이름
     */
    public static final String API_CONTACT_NAME = "COMP Team";

    /**
     * API 라이선스 이름
     */
    public static final String API_LICENSE_NAME = "Apache 2.0";

    /**
     * API 라이선스 URL
     */
    public static final String API_LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0.html";

    // ========== 보안 설정 ==========

    /**
     * Swagger UI에서 사용하는 Bearer 인증 스키마 이름
     * OpenAPI 설정과 @SecurityRequirement 어노테이션에서 동일하게 사용되어야 합니다.
     */
    public static final String BEARER_AUTH_SCHEME = "Bearer Authentication";

    /**
     * Bearer 인증 스키마 설명
     */
    public static final String BEARER_AUTH_DESCRIPTION = "JWT 토큰을 입력하세요 (Bearer 접두사 제외)";

    /**
     * JWT Bearer 토큰 형식
     */
    public static final String BEARER_FORMAT = "JWT";

    // ========== API 경로 패턴 ==========

    /**
     * 인증 관련 API 기본 경로
     */
    public static final String AUTH_API_PATH = "/api/auth";

    /**
     * 인증 관련 API 경로 패턴 (와일드카드)
     */
    public static final String AUTH_API_PATTERN = "/api/auth/**";

    /**
     * OpenAPI 문서 경로 패턴
     */
    public static final String OPENAPI_DOCS_PATTERN = "/v3/api-docs/**";

    /**
     * OpenAPI YAML 문서 경로
     */
    public static final String OPENAPI_YAML_PATH = "/v3/api-docs.yaml";

    /**
     * Swagger UI 리소스 경로 패턴
     */
    public static final String SWAGGER_UI_PATTERN = "/swagger-ui/**";

    /**
     * Swagger UI 메인 페이지 경로
     */
    public static final String SWAGGER_UI_HTML_PATH = "/swagger-ui.html";

    /**
     * Swagger 리소스 경로 패턴
     */
    public static final String SWAGGER_RESOURCES_PATTERN = "/swagger-resources/**";

    /**
     * WebJars 리소스 경로 패턴 (Swagger UI 의존성)
     */
    public static final String WEBJARS_PATTERN = "/webjars/**";

    /**
     * 파비콘 경로
     */
    public static final String FAVICON_PATH = "/favicon.ico";

    /**
     * 에러 페이지 경로
     */
    public static final String ERROR_PATH = "/error";

    private ApiConstants() {
        // 유틸리티 클래스는 인스턴스화 방지
        throw new AssertionError("Cannot instantiate constants class");
    }
}

