package com.comp.comp_web.domain.study.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GroupMemberAttendanceStatus {
    private Long userId;
    private String name;
    private Boolean attend;
    private Boolean late;
    private LocalDateTime attendedAt;
}
