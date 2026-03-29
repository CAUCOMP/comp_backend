package com.comp.comp_web.domain.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Study_Attendance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendace_id")
    private Integer id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(name = "auth_code")
    private Integer authCode;

    @Column(name = "is_attend")
    private Boolean isAttend;

    @Column(name = "late")
    private Boolean late;

    @Column(name = "attended_at")
    private LocalDateTime attendedAt;

    @Builder
    public StudyAttendance(
            Long studentId,
            Integer weekNumber,
            Integer authCode,
            Boolean isAttend,
            Boolean late,
            LocalDateTime attendedAt) {
        this.studentId = studentId;
        this.weekNumber = weekNumber;
        this.authCode = authCode;
        this.isAttend = isAttend;
        this.late = late;
        this.attendedAt = attendedAt;
    }

    public void markAttendance(Integer authCode, boolean late, LocalDateTime attendedAt) {
        this.authCode = authCode;
        this.isAttend = true;
        this.late = late;
        this.attendedAt = attendedAt;
    }
}
