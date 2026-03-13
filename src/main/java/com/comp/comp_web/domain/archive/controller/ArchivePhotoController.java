package com.comp.comp_web.domain.archive.controller;

import com.comp.comp_web.domain.archive.dto.response.ArchivePhotoResponse;
import com.comp.comp_web.domain.archive.service.ArchivePhotoService;
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

@Tag(name = "Archive API", description = "동아리 아카이브(프로젝트, OB, 활동사진) API")
@RestController
@RequestMapping("/archive")
@RequiredArgsConstructor
public class ArchivePhotoController {

    private final ArchivePhotoService archivePhotoService;

    @Operation(summary = "기수별 활동 사진 조회", description = "특정 기수의 활동 사진을 조회합니다. 기수를 보내지 않으면 전체 사진을 최신순으로 반환합니다.")
    @GetMapping("/image") // ⭐️ 명세서 주소대로 매핑!
    public ResponseEntity<List<ArchivePhotoResponse>> getPhotos(
        @Parameter(description = "필터링할 기수 (예: 39)", required = false)
        @RequestParam(value = "generation", required = false) Integer generation) {

        List<ArchivePhotoResponse> response = archivePhotoService.getPhotos(generation);

        return ResponseEntity.ok(response);
    }
}
