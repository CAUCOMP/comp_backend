package com.comp.comp_web.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "data", "error", "timestamp"})
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorDetail error;
    private final LocalDateTime timestamp;

    // 성공 응답
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, LocalDateTime.now());
    }

    // 성공 응답 (데이터 없음)
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, null, LocalDateTime.now());
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String code, String message, String detail) {
        return new ApiResponse<>(false, null, new ErrorDetail(code, message, detail), LocalDateTime.now());
    }

    // 실패 응답 (상세 메시지 없음)
    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, null, new ErrorDetail(code, message, null), LocalDateTime.now());
    }

    // ErrorCode enum을 사용한 실패 응답
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null,
            new ErrorDetail(errorCode.getCode(), errorCode.getMessage(), null),
            LocalDateTime.now());
    }

    // ErrorCode enum을 사용한 실패 응답 (상세 메시지 포함)
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String detail) {
        return new ApiResponse<>(false, null,
            new ErrorDetail(errorCode.getCode(), errorCode.getMessage(), detail),
            LocalDateTime.now());
    }

    public record ErrorDetail(String code, String message, String detail) {
    }
}
