package com.comp.comp_web.domain.auth.controller;

import com.comp.comp_web.domain.auth.dto.*;
import com.comp.comp_web.domain.auth.service.AuthService;
import com.comp.comp_web.global.constants.ApiConstants;
import com.comp.comp_web.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping(ApiConstants.AUTH_API_PATH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "이메일 유효성 확인", description = "EmailDatabase에 존재하는 이메일인지 확인합니다")
    @PostMapping("/check-email")
    public ResponseEntity<ApiResponse<EmailCheckResponse>> checkEmail(
            @Valid @RequestBody EmailCheckRequest request) {
        EmailCheckResponse response = authService.checkEmail(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록하고 Access Token과 Refresh Token을 발급합니다")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signUp(
            @Valid @RequestBody SignUpRequest request) {
        AuthResponse response = authService.signUp(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 Access Token과 Refresh Token을 발급합니다")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
        summary = "토큰 갱신",
        description = "Refresh Token으로 새로운 Access Token과 Refresh Token을 발급합니다 (Refresh Token Rotation)"
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
        summary = "로그아웃",
        description = "사용자의 모든 Refresh Token을 삭제합니다. Authorization 헤더에 Bearer 토큰이 필요합니다.",
        security = @SecurityRequirement(name = ApiConstants.BEARER_AUTH_SCHEME)
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal Long userId) {
        if (userId == null) {
            throw new com.comp.comp_web.global.exception.BusinessException(
                com.comp.comp_web.global.response.ErrorCode.AUTH_003
            );
        }
        authService.logout(userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
