package com.comp.comp_web.domain.archive.dto.response;

import com.comp.comp_web.domain.archive.entity.ArchiveProject;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchiveProjectResponse {

    // 프론트엔드가 명세서에서 요구했던 항목들입니다.
    private Long projectId;
    private Integer generation;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String githubUrl;

    // ⭐️ 핵심: 창고에서 꺼낸 날것의 식재료(Entity)를 예쁜 도시락(DTO)으로 옮겨 담는 메서드
    public static ArchiveProjectResponse from(ArchiveProject project) {
        return ArchiveProjectResponse.builder()
            .projectId(project.getId())
            .generation(project.getGeneration())
            .title(project.getTitle())
            .description(project.getDescription())
            .thumbnailUrl(project.getThumbnailUrl())
            .githubUrl(project.getGithubUrl())
            .build();
    }
}
