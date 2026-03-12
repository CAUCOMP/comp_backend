package com.comp.comp_web.domain.archive.dto.response;

import com.comp.comp_web.domain.archive.entity.ArchiveProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchiveProfileResponse {

    private Long profileId;
    private String name;
    private Integer generation;
    private String company;
    private String jobTitle;
    private String profileImageUrl;

    // ⭐️ 명세서의 "contact": { ... } 중괄호 블록을 만들기 위해,
    // 아래에서 만든 작은 박스(ContactDto)를 통째로 하나의 칸으로 선언합니다!
    private ContactDto contact;

    // 👇 연락처 정보들만 따로 예쁘게 묶기 위한 '작은 도시락통' 설계도입니다.
    @Getter
    @Builder
    public static class ContactDto {
        private String email;
        private String phone;
        private String linkedinUrl;
    }

    // 창고에서 꺼낸 날것의 식재료(Profile)를 예쁜 도시락으로 옮겨 담는 기계
    public static ArchiveProfileResponse from(ArchiveProfile profile) {
        return ArchiveProfileResponse.builder()
            .profileId(profile.getId())
            .name(profile.getName())
            .generation(profile.getGeneration())
            .company(profile.getCompany())
            .jobTitle(profile.getJobTitle())
            .profileImageUrl(profile.getProfileImageUrl())
            // ⭐️ 핵심 가공 포인트:
            // 큰 박스를 조립하는 와중에, 연락처 정보들만 모아서 '작은 박스(ContactDto)'로 먼저 조립한 뒤,
            // 그 완성된 작은 박스를 큰 박스의 contact 칸에 쏙 집어넣습니다!
            .contact(ContactDto.builder()
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .linkedinUrl(profile.getLinkedinUrl())
                .build())
            .build();
    }
}
