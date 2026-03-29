package com.comp.comp_web.domain.study.dto.response;

import com.comp.comp_web.domain.auth.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyMemberResponse {
    private Long userId;
    private String name;
    private String email;
    private Long studyId;

    public static StudyMemberResponse from(User user) {
        return StudyMemberResponse.builder()
            .userId(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .studyId(user.getStudyId())
            .build();
    }
}
