package com.comp.comp_web.domain.archive.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @Entity: "스프링아, 이건 DB 테이블이랑 연결될 식재료(엔티티)야!" 라고 알려줍니다.
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "archive_projects") // 실제 DB에 생성될 테이블 이름입니다.
public class ArchiveProject {

    // @Id: 이 테이블의 고유 번호(주민등록번호) 역할입니다.
    // @GeneratedValue: 데이터가 들어올 때마다 1, 2, 3... 자동으로 번호를 매겨줍니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column: DB 테이블의 각 칸(기둥)을 의미합니다.
    @Column(nullable = false)
    private Integer generation; // 기수 (예: 39)

    @Column(nullable = false, length = 100)
    private String title; // 프로젝트 이름

    @Column(columnDefinition = "TEXT")
    private String description; // 어쩌구저쩌꾸 설명글

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl; // 사진 링크

    @Column(name = "github_url", length = 500)
    private String githubUrl; // 깃허브 링크

    // @Builder: 나중에 이 식재료에 값을 예쁘게 집어넣기 위한 도구입니다.
    @Builder
    public ArchiveProject(Integer generation, String title, String description, String thumbnailUrl, String githubUrl) {
        this.generation = generation;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.githubUrl = githubUrl;
    }
}
