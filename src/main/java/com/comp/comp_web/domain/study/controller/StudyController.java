package com.comp.comp_web.domain.study.controller;

import com.comp.comp_web.domain.study.dto.request.CreateStudyGroupRequest;
import com.comp.comp_web.domain.study.dto.request.PatchStudyRequest;
import com.comp.comp_web.domain.study.dto.request.StudyAttendRequest;
import com.comp.comp_web.domain.study.dto.response.StudyAttendResponse;
import com.comp.comp_web.domain.study.dto.response.StudyGroupAttendanceResponse;
import com.comp.comp_web.domain.study.dto.response.StudyGroupResponse;
import com.comp.comp_web.domain.study.dto.response.StudyMemberResponse;
import com.comp.comp_web.domain.study.service.StudyService;
import com.comp.comp_web.global.constants.ApiConstants;
import com.comp.comp_web.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Study", description = "스터디 관리 API")
@RestController
@RequestMapping(ApiConstants.STUDY_API_PATH)
@RequiredArgsConstructor
@SecurityRequirement(name = ApiConstants.BEARER_AUTH_SCHEME)
public class StudyController {

    private final StudyService studyService;

    @Operation(summary = "스터디 조원 입력용 전체 동아리원 조회")
    @GetMapping("/member")
    public ResponseEntity<ApiResponse<List<StudyMemberResponse>>> getMembers() {
        return ResponseEntity.ok(ApiResponse.success(studyService.getAllMembers()));
    }

    @Operation(summary = "스터디 조 및 스터디 시간 등록")
    @PostMapping("/group")
    public ResponseEntity<ApiResponse<StudyGroupResponse>> createGroup(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateStudyGroupRequest request) {
        StudyGroupResponse response = studyService.createStudyGroup(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "스터디 출석 체크 및 인증 사진 업로드")
    @PostMapping(value = "/attend", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StudyAttendResponse>> attend(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestPart("request") StudyAttendRequest request,
            @RequestPart("image") MultipartFile image) {
        StudyAttendResponse response = studyService.attend(userId, request, image);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "특정 스터디 조 출석/지각 현황 조회")
    @GetMapping("/group-attend")
    public ResponseEntity<ApiResponse<StudyGroupAttendanceResponse>> getGroupAttendance(
            @RequestParam Integer groupId,
            @RequestParam(required = false) Integer weekNumber) {
        StudyGroupAttendanceResponse response = studyService.getGroupAttendance(groupId, weekNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "스터디 정보 수정")
    @PatchMapping("/patch")
    public ResponseEntity<ApiResponse<StudyGroupResponse>> patchStudy(
            @AuthenticationPrincipal Long userId,
            @RequestBody PatchStudyRequest request) {
        StudyGroupResponse response = studyService.patchStudy(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

