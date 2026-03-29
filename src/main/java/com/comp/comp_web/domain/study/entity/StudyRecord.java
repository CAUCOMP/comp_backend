package com.comp.comp_web.domain.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Study_Record")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer id;

    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    @Column(name = "attendance_time")
    private LocalDateTime attendanceTime;

    @Column(name = "attendance_image_url", length = 255)
    private String attendanceImageUrl;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public StudyRecord(
            Integer groupId,
            LocalDateTime attendanceTime,
            String attendanceImageUrl,
            Integer weekNumber,
            String content) {
        this.groupId = groupId;
        this.attendanceTime = attendanceTime;
        this.attendanceImageUrl = attendanceImageUrl;
        this.weekNumber = weekNumber;
        this.content = content;
    }

    public void updateAttendanceRecord(LocalDateTime attendanceTime, String attendanceImageUrl, String content) {
        this.attendanceTime = attendanceTime;
        this.attendanceImageUrl = attendanceImageUrl;
        this.content = content;
    }
}

