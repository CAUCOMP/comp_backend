package com.comp.comp_web.domain.archive.controller;

import com.comp.comp_web.domain.archive.dto.response.ArchiveProfileResponse;
import com.comp.comp_web.domain.archive.service.ArchiveProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 첫 번째 API랑 같은 "Archive API" 태그를 달아두면,
// Swagger 메뉴판에서 프로젝트 API와 프로필 API가 예쁘게 한 묶음으로 정리되어 보입니다!
@Tag(name = "Archive API", description = "동아리 아카이브(프로젝트, OB, 활동사진) API")
@RestController
@RequestMapping("/archive")
@RequiredArgsConstructor
public class ArchiveProfileController {

    private final ArchiveProfileService archiveProfileService;

    @Operation(summary = "OB 선배님 프로필 조회", description = "기수 및 정렬 조건(이름순 등)에 따라 OB 선배님들의 프로필을 반환합니다.")
    @GetMapping("/profile")
    public ResponseEntity<List<ArchiveProfileResponse>> getProfiles(

        // 1. 기수 필터링 주문 받기 (?generation=39)
        @Parameter(description = "필터링할 기수 (예: 39)", required = false)
        @RequestParam(value = "generation", required = false) Integer generation,

        // 2. 정렬 조건 주문 받기 (?sort=nameAsc)
        @Parameter(description = "정렬 조건 (예: nameAsc - 이름 오름차순. 안 보내면 기수 내림차순 기본적용)", required = false)
        @RequestParam(value = "sort", required = false) String sort) {

        // 주방장(Service)에게 손님이 요구한 기수와 정렬 방식을 그대로 전달합니다!
        List<ArchiveProfileResponse> response = archiveProfileService.getProfiles(generation, sort);

        // 완성된 요리를 200 OK 상태 코드와 함께 서빙합니다.
        return ResponseEntity.ok(response);
    }
}
