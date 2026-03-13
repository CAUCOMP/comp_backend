package com.comp.comp_web.domain.archive.service;

import com.comp.comp_web.domain.archive.dto.response.ArchivePhotoResponse;
import com.comp.comp_web.domain.archive.entity.ArchivePhoto;
import com.comp.comp_web.domain.archive.repository.ArchivePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArchivePhotoService {

    private final ArchivePhotoRepository archivePhotoRepository;

    public List<ArchivePhotoResponse> getPhotos(Integer generation) {

        List<ArchivePhoto> photos;

        // ⭐️ 명세서 로직 완벽 구현: 기수가 있으면 해당 기수만, 없으면 전체 반환!
        if (generation != null) {
            photos = archivePhotoRepository.findByGenerationOrderByIdDesc(generation);
        } else {
            photos = archivePhotoRepository.findAllByOrderByGenerationDescIdDesc();
        }

        // 컨베이어 벨트를 돌려 DTO로 변환
        return photos.stream()
            .map(ArchivePhotoResponse::from)
            .collect(Collectors.toList());
    }
}
