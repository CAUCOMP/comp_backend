package com.comp.comp_web.domain.study.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyAttendRequest {

    @NotNull
    private Integer weekNumber;

    private Integer authCode;

    private String content;
}
