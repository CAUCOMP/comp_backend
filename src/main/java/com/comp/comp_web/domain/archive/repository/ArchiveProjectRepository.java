package com.comp.comp_web.domain.archive.repository;

import com.comp.comp_web.domain.archive.entity.ArchiveProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// JpaRepository<관리할 엔티티, 엔티티의 ID 타입(Long)>을 상속받으면 기본 세팅 끝!
public interface ArchiveProjectRepository extends JpaRepository<ArchiveProject, Long> {

    // 1. 특정 기수(generation)만 찾아서 최신순 정렬 (예: ?generation=39)
    List<ArchiveProject> findByGenerationOrderByGenerationDesc(Integer generation);

    // 2. 전체 프로젝트를 최신 기수부터 내림차순 정렬 (기수를 안 보냈을 때)
    List<ArchiveProject> findAllByOrderByGenerationDesc();
}
