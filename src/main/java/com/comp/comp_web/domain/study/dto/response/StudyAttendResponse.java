package com.comp.comp_web.domain.study.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyAttendResponse {
    private Long userId;
    private Integer groupId;
    private Integer weekNumber;
    private Boolean attend;
    private Boolean late;
    private LocalDateTime attendedAt;
    private String attendanceImageUrl;
}
