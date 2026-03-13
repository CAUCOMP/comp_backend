package com.comp.comp_web.domain.archive.dto.response;

import com.comp.comp_web.domain.archive.entity.ArchivePhoto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchivePhotoResponse {

    private Long imageId; // 명세서대로 imageId로 이름 맞춤!
    private Integer generation;
    private String imageUrl;
    private String title;
    private String description;

    // 1번 문에서 나온 날것의 식재료를 예쁜 도시락으로 옮겨 담는 기계
    public static ArchivePhotoResponse from(ArchivePhoto photo) {
        return ArchivePhotoResponse.builder()
            .imageId(photo.getId())
            .generation(photo.getGeneration())
            .imageUrl(photo.getImageUrl())
            .title(photo.getTitle())
            .description(photo.getDescription())
            .build();
    }
}
