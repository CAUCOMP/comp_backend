package com.comp.comp_web.domain.auth.dto;

import com.comp.comp_web.domain.auth.entity.Role;
import com.comp.comp_web.domain.auth.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean isVerified;
    private String status;
    private LocalDateTime createdAt;
    private Long studyId;

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole())
            .isVerified(user.getIsVerified())
            .status(user.getStatus())
            .createdAt(user.getCreatedAt())
            .studyId(user.getStudyId())
            .build();
    }
}
