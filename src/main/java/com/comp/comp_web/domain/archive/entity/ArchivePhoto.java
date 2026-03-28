package com.comp.comp_web.domain.archive.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "archive_photos")
public class ArchivePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 명세서의 imageId 역할

    @Column(nullable = false)
    private Integer generation; // 기수

    @Column(nullable = false, length = 100)
    private String title; // 사진 제목 (예: "OT")

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl; // 사진 링크

    // ⭐️ 명세서 반영: 사진 설명 추가!
    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public ArchivePhoto(Integer generation, String title, String imageUrl, String description) {
        this.generation = generation;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
