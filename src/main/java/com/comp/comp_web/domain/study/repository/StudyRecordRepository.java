package com.comp.comp_web.domain.study.repository;

import com.comp.comp_web.domain.study.entity.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Integer> {
    Optional<StudyRecord> findByGroupIdAndWeekNumber(Integer groupId, Integer weekNumber);
}
