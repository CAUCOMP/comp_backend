package com.comp.comp_web.domain.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Study_Group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer id;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @Column(name = "study_time")
    private LocalDateTime studyTime;

    @Column(nullable = false)
    private Integer generation;

    @Column(name = "member_count", nullable = false)
    private Integer memberCount;

    @Builder
    public StudyGroup(String groupName, LocalDateTime studyTime, Integer generation, Integer memberCount) {
        this.groupName = groupName;
        this.studyTime = studyTime;
        this.generation = generation;
        this.memberCount = memberCount;
    }

    public void updateGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void updateStudyTime(LocalDateTime studyTime) {
        this.studyTime = studyTime;
    }

    public void updateMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}

