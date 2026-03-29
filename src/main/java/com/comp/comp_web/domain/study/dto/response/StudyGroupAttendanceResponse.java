package com.comp.comp_web.domain.study.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyGroupAttendanceResponse {
    private Integer groupId;
    private Integer weekNumber;
    private Integer totalMembers;
    private Integer attendedCount;
    private Integer lateCount;
    private List<GroupMemberAttendanceStatus> members;
}
