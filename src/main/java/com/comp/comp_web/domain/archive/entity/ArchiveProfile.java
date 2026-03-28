package com.comp.comp_web.domain.archive.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "archive_profiles")
public class ArchiveProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 번호 (명세서의 profileId 역할)

    @Column(nullable = false)
    private Integer generation; // 기수

    @Column(nullable = false, length = 50)
    private String name; // 이름

    @Column(length = 100)
    private String company; // 소속/직장

    // ⭐️ 명세서 반영: 직무 추가
    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl; // 프로필 사진

    // ⭐️ 명세서 반영: 연락처 정보들 (DB에는 이렇게 각각의 기둥으로 저장합니다)
    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Builder
    public ArchiveProfile(Integer generation, String name, String company, String jobTitle, String profileImageUrl, String email, String phone, String linkedinUrl) {
        this.generation = generation;
        this.name = name;
        this.company = company;
        this.jobTitle = jobTitle;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.phone = phone;
        this.linkedinUrl = linkedinUrl;
    }
}
