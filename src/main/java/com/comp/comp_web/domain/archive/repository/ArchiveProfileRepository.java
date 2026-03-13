package com.comp.comp_web.domain.archive.repository;

import com.comp.comp_web.domain.archive.entity.ArchiveProfile;
import org.springframework.data.domain.Sort; // ⭐️ Sort 도구를 추가로 가져옵니다!
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArchiveProfileRepository extends JpaRepository<ArchiveProfile, Long> {

    // 1. 특정 기수를 찾되, 정렬 기준(Sort)은 주방장(Service)이 넘겨주는 대로 따르겠다!
    List<ArchiveProfile> findByGeneration(Integer generation, Sort sort);

    // 💡 참고: 전체를 조회하면서 정렬하는 findAll(Sort sort) 기능은
    // JpaRepository 안에 이미 기본적으로 탑재되어 있어서 우리가 여기에 굳이 안 적어도 됩니다!
}
