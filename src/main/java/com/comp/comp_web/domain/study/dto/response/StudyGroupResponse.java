package com.comp.comp_web.domain.study.dto.response;

import com.comp.comp_web.domain.study.entity.StudyGroup;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyGroupResponse {
    private Integer groupId;
    private String groupName;
    private LocalDateTime studyTime;
    private Integer generation;
    private Integer memberCount;

    public static StudyGroupResponse from(StudyGroup group) {
        return StudyGroupResponse.builder()
            .groupId(group.getId())
            .groupName(group.getGroupName())
            .studyTime(group.getStudyTime())
            .generation(group.getGeneration())
            .memberCount(group.getMemberCount())
            .build();
    }
}

