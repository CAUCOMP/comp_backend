package com.comp.comp_web.domain.auth.controller;

import com.comp.comp_web.domain.auth.dto.EmailCheckRequest;
import com.comp.comp_web.domain.auth.dto.EmailCheckResponse;
import com.comp.comp_web.domain.auth.service.AuthService;
import com.comp.comp_web.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
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
}
