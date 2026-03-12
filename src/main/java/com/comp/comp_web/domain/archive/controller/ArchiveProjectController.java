package com.comp.comp_web.domain.archive.controller;

import com.comp.comp_web.domain.archive.dto.response.ArchiveProjectResponse;
import com.comp.comp_web.domain.archive.service.ArchiveProjectService;
// Swagger를 위한 임포트 추가!
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Tag: Swagger 문서에서 이 컨트롤러가 어떤 묶음인지 제목을 달아줍니다.
@Tag(name = "Archive API", description = "동아리 아카이브(프로젝트, OB, 활동사진) API")
@RestController
@RequestMapping("/archive")
@RequiredArgsConstructor
public class ArchiveProjectController {

    private final ArchiveProjectService archiveProjectService;

    // @Operation: 이 특정 API가 무슨 기능을 하는지 자세한 설명을 적습니다.
    @Operation(summary = "기수별 과거 프로젝트 조회", description = "기수를 입력하면 해당 기수의 프로젝트를, 입력하지 않으면 전체 프로젝트를 최신순으로 반환합니다.")
    @GetMapping("/projects")
    public ResponseEntity<List<ArchiveProjectResponse>> getProjects(
        // @Parameter: 주소창으로 받을 값(파라미터)에 대한 설명을 적습니다.
        @Parameter(description = "필터링할 기수 (예: 39)", required = false)
        @RequestParam(value = "generation", required = false) Integer generation) {

        List<ArchiveProjectResponse> response = archiveProjectService.getProjects(generation);
        return ResponseEntity.ok(response);
    }
}
