package com.comp.comp_web.domain.study.repository;

import com.comp.comp_web.domain.study.entity.StudyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudyAttendanceRepository extends JpaRepository<StudyAttendance, Integer> {
    Optional<StudyAttendance> findByStudentIdAndWeekNumber(Long studentId, Integer weekNumber);

    List<StudyAttendance> findAllByStudentIdInAndWeekNumber(Collection<Long> studentIds, Integer weekNumber);

    Optional<StudyAttendance> findTopByStudentIdInOrderByWeekNumberDesc(Collection<Long> studentIds);
}

