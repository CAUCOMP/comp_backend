package com.comp.comp_web.domain.archive.repository;

import com.comp.comp_web.domain.archive.entity.ArchivePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArchivePhotoRepository extends JpaRepository<ArchivePhoto, Long> {

    // 1. 특정 기수의 사진 조회 (가장 최근에 올린 사진부터 보여주기 위해 OrderByIdDesc 추가)
    List<ArchivePhoto> findByGenerationOrderByIdDesc(Integer generation);

    // 2. 전체 사진 조회 (최신 기수 먼저, 그리고 최신 사진 먼저)
    List<ArchivePhoto> findAllByOrderByGenerationDescIdDesc();
}
