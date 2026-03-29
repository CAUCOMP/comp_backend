package com.comp.comp_web.domain.study.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateStudyGroupRequest {

    @NotBlank
    private String groupName;

    @NotNull
    private LocalDateTime studyTime;

    @NotNull
    private Integer generation;

    @NotNull
    private Long leaderId;

    @NotEmpty
    private List<Long> memberIds;
}

