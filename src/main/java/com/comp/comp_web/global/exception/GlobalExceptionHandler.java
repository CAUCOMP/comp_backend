package com.comp.comp_web.global.exception;

import com.comp.comp_web.global.response.ApiResponse;
import com.comp.comp_web.global.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: code={}, message={}, detail={}",
            e.getErrorCode().getCode(), e.getMessage(), e.getDetail());

        ApiResponse<Void> response = e.getDetail() != null
            ? ApiResponse.error(e.getErrorCode(), e.getDetail())
            : ApiResponse.error(e.getErrorCode());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    // 인증 실패 처리
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException e) {
        log.warn("AuthenticationException: {}", e.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
            ErrorCode.AUTH_003,
            "다시 로그인 후 시도해주세요."
        );

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(response);
    }

    // 권한 없음 처리
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("AccessDeniedException: {}", e.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
            ErrorCode.AUTH_004,
            "해당 리소스에 접근할 권한이 없습니다."
        );

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(response);
    }

    // Validation 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String detail = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        log.warn("MethodArgumentNotValidException: {}", detail);

        ApiResponse<Void> response = ApiResponse.error(
            ErrorCode.VALID_004,
            detail
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
            ErrorCode.COMMON_001,
            e.getMessage()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    // 기타 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected Exception", e);

        ApiResponse<Void> response = ApiResponse.error(
            ErrorCode.COMMON_003,
            "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
        );

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}
