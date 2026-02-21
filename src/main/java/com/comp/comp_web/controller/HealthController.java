package com.comp.comp_web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Health Check", description = "애플리케이션 상태 확인 API")
public class HealthController {
    @GetMapping("/health")
    @Operation(summary = "헬스 체크", description = "애플리케이션의 상태를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "정상 작동 중")
    })
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Application is running successfully!");
        return response;
    }

    @GetMapping("/hello")
    @Operation(summary = "hello world", description = "hello world 메시지를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 메시지 반환")
    })
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, COMP!");
        return response;
    }
}
