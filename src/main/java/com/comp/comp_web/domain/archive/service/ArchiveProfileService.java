package com.comp.comp_web.domain.archive.service;

import com.comp.comp_web.domain.archive.dto.response.ArchiveProfileResponse;
import com.comp.comp_web.domain.archive.entity.ArchiveProfile;
import com.comp.comp_web.domain.archive.repository.ArchiveProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 데이터 조회만 하니까 속도 향상을 위해 읽기 전용 모드 켬!
public class ArchiveProfileService {

    private final ArchiveProfileRepository archiveProfileRepository;

    // 웨이터(Controller)가 호출할 메인 요리 버튼
    public List<ArchiveProfileResponse> getProfiles(Integer generation, String sortType) {

        // 1. 프론트엔드가 보낸 정렬 글자(sortType)를 보고, 로봇이 알아들을 수 있는 Sort 규격표로 변환합니다.
        Sort sort = createSort(sortType);

        List<ArchiveProfile> profiles;

        // 2. 주문서에 기수(generation)가 적혀있는지 확인하고 창고 로봇에게 지시합니다.
        if (generation != null) {
            // 특정 기수만 가져오기 (정렬 기준표 포함)
            profiles = archiveProfileRepository.findByGeneration(generation, sort);
        } else {
            // 전체 다 가져오기 (정렬 기준표 포함, findAll은 JpaRepository가 기본으로 가진 마법의 기능입니다)
            profiles = archiveProfileRepository.findAll(sort);
        }

        // 3. 컨베이어 벨트(stream)를 돌려서 날것의 식재료를 예쁜 도시락(DTO)으로 하나씩 옮겨 담아 반환합니다.
        return profiles.stream()
            .map(ArchiveProfileResponse::from)
            .collect(Collectors.toList());
    }

    // 👇 [헬퍼 메서드] 프론트엔드의 텍스트("nameAsc")를 스프링의 Sort 객체로 바꿔주는 번역기
    private Sort createSort(String sortType) {
        // 만약 프론트엔드가 "nameAsc" (이름 오름차순)를 원했다면?
        if ("nameAsc".equals(sortType)) {
            return Sort.by(Sort.Direction.ASC, "name");
        }

        // 그 외의 경우 (아무것도 안 보냈거나 이상한 글자를 보냈을 때) 기본값: 기수 내림차순(최신 기수 먼저)
        return Sort.by(Sort.Direction.DESC, "generation");
    }
}
