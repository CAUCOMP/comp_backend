package com.comp.comp_web.domain.archive.service;

import com.comp.comp_web.domain.archive.dto.response.ArchiveProjectResponse;
import com.comp.comp_web.domain.archive.entity.ArchiveProject;
import com.comp.comp_web.domain.archive.repository.ArchiveProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// @Service: "스프링아, 얘는 비즈니스 로직을 담당하는 주방장이야!" 라고 등록합니다.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회만 하는 기능이므로 '읽기 전용'으로 세팅해서 속도를 높입니다.
public class ArchiveProjectService {

    // 주방장이 창고 로봇을 호출할 수 있게 연결해 줍니다.
    private final ArchiveProjectRepository archiveProjectRepository;

    // 웨이터(Controller)가 호출할 요리 시작 버튼(메서드)입니다.
    public List<ArchiveProjectResponse> getProjects(Integer generation) {
        List<ArchiveProject> projects;

        // 1. 손님이 주문할 때 특정 기수(generation)를 불렀는지 확인합니다.
        if (generation != null) {
            // 특정 기수의 프로젝트만 창고에서 가져옵니다.
            projects = archiveProjectRepository.findByGenerationOrderByGenerationDesc(generation);
        } else {
            // 아무 말 안 했으면 전체 프로젝트를 창고에서 싹 다 가져옵니다.
            projects = archiveProjectRepository.findAllByOrderByGenerationDesc();
        }

        // 2. 창고에서 꺼낸 날것의 식재료(Entity)들을 줄 세워서(stream),
        // 예쁜 도시락(DTO)으로 하나씩 옮겨 담고(map), 다시 하나의 상자로 묶어서(collect) 반환합니다.
        return projects.stream()
            .map(ArchiveProjectResponse::from)
            .collect(Collectors.toList());
    }
}
