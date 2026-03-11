-- ============================================
-- COMP Backend Database Schema
-- Spring Boot JPA 엔티티와 완벽 매핑
-- MySQL 8.0, InnoDB, UTF8MB4
-- ============================================

-- User 테이블 (엔티티: com.comp.comp_web.domain.auth.entity.User)
CREATE TABLE `User` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) NOT NULL COMMENT '중복 가입 차단',
    `password` VARCHAR(255) NOT NULL COMMENT 'BCrypt 암호화',
    `role` VARCHAR(20) NULL DEFAULT 'User' COMMENT 'User, Admin',
    `refresh_token` VARCHAR(500) NULL COMMENT 'JWT Refresh Token',
    `is_verified` TINYINT(1) NULL DEFAULT 1 COMMENT '이메일 인증 (1: 즉시 활성화)',
    `status` VARCHAR(20) NULL DEFAULT 'ACTIVE' COMMENT '가입 상태',
    `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '회원가입시간',
    `study_id` BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_email` (`email`),
    INDEX `idx_user_email` (`email`),
    INDEX `idx_user_status` (`status`),
    INDEX `idx_user_study_id` (`study_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자';

-- RefreshToken 테이블 (엔티티: com.comp.comp_web.domain.auth.entity.RefreshToken)
CREATE TABLE `RefreshToken` (
    `id` VARCHAR(255) NOT NULL COMMENT 'UUID',
    `user_id` BIGINT NOT NULL,
    `token_hash` VARCHAR(255) NOT NULL COMMENT 'SHA-256 해시',
    `expires_at` DATETIME(6) NOT NULL,
    `revoked_at` DATETIME(6) NULL,
    `created_at` DATETIME(6) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_refreshtoken_user_id` (`user_id`),
    INDEX `idx_refreshtoken_token_hash` (`token_hash`),
    INDEX `idx_refreshtoken_expires_at` (`expires_at`),
    CONSTRAINT `fk_refreshtoken_user` FOREIGN KEY (`user_id`)
        REFERENCES `User` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='JWT Refresh Token';

-- EmailDatabase 테이블 (엔티티: com.comp.comp_web.domain.auth.entity.EmailDatabase)
CREATE TABLE `EmailDatabase` (
    `email` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NULL,
    PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이메일 검증';

-- Study_Group 테이블
CREATE TABLE `Study_Group` (
    `group_id` INT NOT NULL AUTO_INCREMENT,
    `group_name` VARCHAR(50) NOT NULL,
    `study_time` DATETIME(6) NULL,
    `generation` INT NOT NULL,
    `member_count` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`group_id`),
    INDEX `idx_studygroup_generation` (`generation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='스터디 그룹';

-- Study_Attendance 테이블
CREATE TABLE `Study_Attendance` (
    `attendace_id` INT NOT NULL AUTO_INCREMENT,
    `student_id` BIGINT NOT NULL COMMENT 'User 테이블 참조',
    `week_number` INT NOT NULL,
    `auth_code` INT NULL COMMENT '인증번호',
    `is_attend` TINYINT(1) NULL DEFAULT 0 COMMENT '출석 체크',
    `late` BOOLEAN NULL DEFAULT FALSE,
    `attended_at` DATETIME NULL COMMENT '인증번호 입력 시간',
    PRIMARY KEY (`attendace_id`),
    INDEX `idx_studyattendance_student_id` (`student_id`),
    INDEX `idx_studyattendance_week_number` (`week_number`),
    CONSTRAINT `fk_studyattendance_user` FOREIGN KEY (`student_id`)
        REFERENCES `User` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='스터디 출석';

-- Study_Record 테이블
CREATE TABLE `Study_Record` (
    `record_id` INT NOT NULL AUTO_INCREMENT,
    `group_id` INT NOT NULL,
    `attendance_time` DATETIME(6) NULL COMMENT '출석 시간',
    `attendance_image_url` VARCHAR(255) NULL COMMENT '출석 인증 이미지',
    `week_number` INT NOT NULL,
    `content` TEXT NOT NULL COMMENT '조별 활동 요약',
    PRIMARY KEY (`record_id`),
    INDEX `idx_studyrecord_group_id` (`group_id`),
    INDEX `idx_studyrecord_week_number` (`week_number`),
    CONSTRAINT `fk_studyrecord_group` FOREIGN KEY (`group_id`)
        REFERENCES `Study_Group` (`group_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='스터디 기록';

-- Study_Assignment 테이블
CREATE TABLE `Study_Assignment` (
    `assignment_id` INT NOT NULL AUTO_INCREMENT,
    `student_id` BIGINT NOT NULL COMMENT 'User 테이블 참조',
    `week_number` INT NOT NULL,
    `file_url` VARCHAR(255) NULL,
    `is_submitted` TINYINT(1) NULL DEFAULT 0 COMMENT '제출 여부',
    PRIMARY KEY (`assignment_id`),
    INDEX `idx_studyassignment_student_id` (`student_id`),
    INDEX `idx_studyassignment_week_number` (`week_number`),
    CONSTRAINT `fk_studyassignment_user` FOREIGN KEY (`student_id`)
        REFERENCES `User` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='스터디 과제';

-- Curriculum 테이블
CREATE TABLE `Curriculum` (
    `curriculum_id` INT NOT NULL AUTO_INCREMENT,
    `generation` INT NULL,
    `title` VARCHAR(100) NULL,
    `content` TEXT NULL COMMENT '활동 설명',
    `order_index` INT NULL COMMENT '표시 순서',
    `image_url` VARCHAR(255) NULL COMMENT '이미지 URL',
    PRIMARY KEY (`curriculum_id`),
    INDEX `idx_curriculum_generation` (`generation`),
    INDEX `idx_curriculum_order_index` (`order_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='커리큘럼';

-- FAQ 테이블
CREATE TABLE `FAQ` (
    `faq_id` INT NOT NULL AUTO_INCREMENT,
    `category` VARCHAR(50) NULL COMMENT '지원관련, 활동관련, 기타',
    `question` TEXT NOT NULL,
    `answer` TEXT NOT NULL,
    `priority` INT NULL COMMENT '표시 순서',
    PRIMARY KEY (`faq_id`),
    INDEX `idx_faq_category` (`category`),
    INDEX `idx_faq_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='FAQ';

-- Attendance 테이블
CREATE TABLE `Attendance` (
    `attendance_id` INT NOT NULL AUTO_INCREMENT,
    `student_id` BIGINT NOT NULL COMMENT 'User 테이블 참조',
    `attendance_date` DATE NULL,
    `status` VARCHAR(20) NULL,
    PRIMARY KEY (`attendance_id`),
    INDEX `idx_attendance_student_id` (`student_id`),
    INDEX `idx_attendance_date` (`attendance_date`),
    CONSTRAINT `fk_attendance_user` FOREIGN KEY (`student_id`)
        REFERENCES `User` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='출석';

-- Alumni 테이블
CREATE TABLE `Alumni` (
    `alumni_id` INT NOT NULL AUTO_INCREMENT,
    `student_id` BIGINT NOT NULL COMMENT 'User 테이블 참조',
    `company` VARCHAR(100) NULL,
    `job` VARCHAR(50) NULL,
    `contact` VARCHAR(100) NULL,
    `profile_image_url` VARCHAR(500) NULL,
    `message` TEXT NULL COMMENT '소개 메시지',
    `is_public` TINYINT(1) NULL DEFAULT 1 COMMENT '공개 여부',
    PRIMARY KEY (`alumni_id`),
    INDEX `idx_alumni_student_id` (`student_id`),
    INDEX `idx_alumni_is_public` (`is_public`),
    CONSTRAINT `fk_alumni_user` FOREIGN KEY (`student_id`)
        REFERENCES `User` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='동문';

-- Executive_Info 테이블
CREATE TABLE `Executive_Info` (
    `exec_id` INT NOT NULL AUTO_INCREMENT,
    `student_id` BIGINT NOT NULL COMMENT 'User 테이블 참조',
    `position` VARCHAR(50) NULL,
    `intro_message` VARCHAR(255) NULL,
    `profile_image_url` VARCHAR(500) NULL,
    PRIMARY KEY (`exec_id`),
    INDEX `idx_executive_student_id` (`student_id`),
    CONSTRAINT `fk_executive_user` FOREIGN KEY (`student_id`)
        REFERENCES `User` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='임원 정보';

-- Recruitment 테이블
CREATE TABLE `Recruitment` (
    `recruitment_id` INT NOT NULL AUTO_INCREMENT,
    `generation` INT NULL,
    `status` TINYINT NULL COMMENT '모집 상태 (0: OFF, 1: ON)',
    `doc_start_date` DATETIME NULL COMMENT '서류 시작일',
    `doc_end_date` DATETIME NULL COMMENT '서류 종료일',
    `doc_result_date` DATETIME NULL COMMENT '서류 발표일',
    `interview_start_date` DATETIME NULL COMMENT '면접 시작일',
    `interview_end_date` DATETIME NULL COMMENT '면접 종료일',
    `final_result_date` DATETIME NULL COMMENT '최종 발표일',
    `google_form_url` VARCHAR(255) NULL,
    PRIMARY KEY (`recruitment_id`),
    INDEX `idx_recruitment_generation` (`generation`),
    INDEX `idx_recruitment_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='모집';

-- Project 테이블
CREATE TABLE `Project` (
    `project_id` INT NOT NULL AUTO_INCREMENT,
    `generation` INT NOT NULL,
    `project_type` VARCHAR(20) NOT NULL,
    `title` VARCHAR(100) NOT NULL,
    `description` VARCHAR(255) NULL,
    `thumbnail_url` VARCHAR(255) NOT NULL,
    `content` TEXT NULL,
    `created_at` DATE NULL DEFAULT (CURRENT_DATE) COMMENT '생성일',
    PRIMARY KEY (`project_id`),
    INDEX `idx_project_generation` (`generation`),
    INDEX `idx_project_type` (`project_type`),
    INDEX `idx_project_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='프로젝트';

-- Club_Settings 테이블
CREATE TABLE `Club_Settings` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `club_name` VARCHAR(50) NULL,
    `logo_url` VARCHAR(255) NULL,
    `slogan` VARCHAR(255) NULL,
    `since_year` INT NULL,
    `total_generations` INT NULL,
    `total_members_count` INT NULL,
    `club_description` VARCHAR(255) NULL,
    `recruitment_limit` INT NULL,
    `github_url` VARCHAR(255) NULL,
    `insta_url` VARCHAR(255) NULL,
    `kakao_url` VARCHAR(255) NULL,
    `copyright_text` VARCHAR(255) NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='동아리 설정';

-- ============================================
-- CONSTRAINTS 요약
-- ============================================

-- PRIMARY KEY CONSTRAINTS (15개)
-- ✅ User.id (BIGINT AUTO_INCREMENT)
-- ✅ RefreshToken.id (VARCHAR UUID)
-- ✅ EmailDatabase.email (VARCHAR)
-- ✅ Study_Group.group_id (INT AUTO_INCREMENT)
-- ✅ Study_Attendance.attendace_id (INT AUTO_INCREMENT)
-- ✅ Study_Record.record_id (INT AUTO_INCREMENT)
-- ✅ Study_Assignment.assignment_id (INT AUTO_INCREMENT)
-- ✅ Curriculum.curriculum_id (INT AUTO_INCREMENT)
-- ✅ FAQ.faq_id (INT AUTO_INCREMENT)
-- ✅ Attendance.attendance_id (INT AUTO_INCREMENT)
-- ✅ Alumni.alumni_id (INT AUTO_INCREMENT)
-- ✅ Executive_Info.exec_id (INT AUTO_INCREMENT)
-- ✅ Recruitment.recruitment_id (INT AUTO_INCREMENT)
-- ✅ Project.project_id (INT AUTO_INCREMENT)
-- ✅ Club_Settings.id (INT AUTO_INCREMENT)

-- UNIQUE KEY CONSTRAINTS (1개)
-- ✅ User.email (uk_user_email) - 이메일 중복 방지

-- FOREIGN KEY CONSTRAINTS (7개) ⬆️ 증가!
-- ✅ RefreshToken.user_id → User.id (fk_refreshtoken_user)
--    ON DELETE CASCADE: User 삭제 시 RefreshToken 자동 삭제
--    ON UPDATE CASCADE: User.id 변경 시 RefreshToken.user_id 자동 업데이트
--
-- ✅ Study_Attendance.student_id → User.id (fk_studyattendance_user)
--    ON DELETE CASCADE: User 삭제 시 Study_Attendance 자동 삭제
--    ON UPDATE CASCADE: User.id 변경 시 student_id 자동 업데이트
--
-- ✅ Study_Record.group_id → Study_Group.group_id (fk_studyrecord_group)
--    ON DELETE CASCADE: Study_Group 삭제 시 Study_Record 자동 삭제
--    ON UPDATE CASCADE: Study_Group.group_id 변경 시 Study_Record.group_id 자동 업데이트
--
-- ✅ Study_Assignment.student_id → User.id (fk_studyassignment_user)
--    ON DELETE CASCADE: User 삭제 시 Study_Assignment 자동 삭제
--    ON UPDATE CASCADE: User.id 변경 시 student_id 자동 업데이트
--
-- ✅ Attendance.student_id → User.id (fk_attendance_user)
--    ON DELETE CASCADE: User 삭제 시 Attendance 자동 삭제
--    ON UPDATE CASCADE: User.id 변경 시 student_id 자동 업데이트
--
-- ✅ Alumni.student_id → User.id (fk_alumni_user)
--    ON DELETE CASCADE: User 삭제 시 Alumni 자동 삭제
--    ON UPDATE CASCADE: User.id 변경 시 student_id 자동 업데이트
--
-- ✅ Executive_Info.student_id → User.id (fk_executive_user)
--    ON DELETE CASCADE: User 삭제 시 Executive_Info 자동 삭제
--    ON UPDATE CASCADE: User.id 변경 시 student_id 자동 업데이트

-- INDEX CONSTRAINTS (32개) ⬆️ 증가!
-- User: idx_user_email, idx_user_status, idx_user_study_id (3개)
-- RefreshToken: idx_refreshtoken_user_id, idx_refreshtoken_token_hash, idx_refreshtoken_expires_at (3개)
-- Study_Group: idx_studygroup_generation (1개)
-- Study_Attendance: idx_studyattendance_student_id, idx_studyattendance_week_number (2개)
-- Study_Record: idx_studyrecord_group_id, idx_studyrecord_week_number (2개)
-- Study_Assignment: idx_studyassignment_student_id, idx_studyassignment_week_number (2개)
-- Curriculum: idx_curriculum_generation, idx_curriculum_order_index (2개)
-- FAQ: idx_faq_category, idx_faq_priority (2개)
-- Attendance: idx_attendance_student_id, idx_attendance_date (2개)
-- Alumni: idx_alumni_student_id, idx_alumni_is_public (2개)
-- Executive_Info: idx_executive_student_id (1개)
-- Recruitment: idx_recruitment_generation, idx_recruitment_status (2개)
-- Project: idx_project_generation, idx_project_type, idx_project_created_at (3개)
-- + FK 인덱스 자동 생성 (7개)

-- ============================================
-- 데이터베이스 관계도
-- ============================================
--
--                    ┌─────────────────┐
--                    │      User       │
--                    │  (id: BIGINT)   │
--                    └────────┬────────┘
--                             │
--        ┌────────────────────┼────────────────────────┐
--        │                    │                        │
--        │ 1:N                │ 1:N                    │ 1:N
--        ▼                    ▼                        ▼
-- ┌──────────────┐   ┌─────────────────┐    ┌─────────────────┐
-- │ RefreshToken │   │Study_Attendance │    │Study_Assignment │
-- └──────────────┘   └─────────────────┘    └─────────────────┘
--
--        │ 1:N                │ 1:N                    │ 1:N
--        ▼                    ▼                        ▼
-- ┌──────────────┐   ┌─────────────────┐    ┌─────────────────┐
-- │  Attendance  │   │     Alumni      │    │ Executive_Info  │
-- └──────────────┘   └─────────────────┘    └─────────────────┘
--
--
-- ┌─────────────────┐
-- │  Study_Group    │
-- │ (group_id: INT) │
-- └────────┬────────┘
--          │
--          │ 1:N
--          ▼
-- ┌─────────────────┐
-- │  Study_Record   │
-- └─────────────────┘
--
-- 독립 테이블 (FK 없음):
-- - EmailDatabase (이메일 검증용)
-- - Curriculum (커리큘럼 관리)
-- - FAQ (자주 묻는 질문)
-- - Recruitment (모집 관리)
-- - Project (프로젝트 관리)
-- - Club_Settings (동아리 설정)

-- ============================================
-- 참고사항
-- ============================================
-- 1. AUTO_INCREMENT: 13개 테이블 (RefreshToken, EmailDatabase 제외)
-- 2. ENGINE: InnoDB (트랜잭션, 외래 키 지원)
-- 3. CHARSET: utf8mb4 (이모지 및 다국어 지원)
-- 4. COLLATE: utf8mb4_unicode_ci (대소문자 구분 없는 정렬)
-- 5. student_id 타입 변경: INT → BIGINT (User.id와 일치)
-- 6. CASCADE 정책: User 삭제 시 관련 모든 데이터 자동 삭제
-- 7. 참조 무결성: 7개 FK로 데이터 일관성 보장

