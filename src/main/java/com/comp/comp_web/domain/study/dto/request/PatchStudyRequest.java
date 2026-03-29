package com.comp.comp_web.domain.study.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PatchStudyRequest {

    private Integer groupId;
    private String groupName;
    private LocalDateTime studyTime;
    private List<Long> memberIds;
}
