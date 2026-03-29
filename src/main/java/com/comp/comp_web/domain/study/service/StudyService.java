package com.comp.comp_web.domain.study.service;

import com.comp.comp_web.domain.auth.entity.User;
import com.comp.comp_web.domain.auth.repository.UserRepository;
import com.comp.comp_web.domain.study.dto.request.CreateStudyGroupRequest;
import com.comp.comp_web.domain.study.dto.request.PatchStudyRequest;
import com.comp.comp_web.domain.study.dto.request.StudyAttendRequest;
import com.comp.comp_web.domain.study.dto.response.GroupMemberAttendanceStatus;
import com.comp.comp_web.domain.study.dto.response.StudyAttendResponse;
import com.comp.comp_web.domain.study.dto.response.StudyGroupAttendanceResponse;
import com.comp.comp_web.domain.study.dto.response.StudyGroupResponse;
import com.comp.comp_web.domain.study.dto.response.StudyMemberResponse;
import com.comp.comp_web.domain.study.entity.StudyAttendance;
import com.comp.comp_web.domain.study.entity.StudyGroup;
import com.comp.comp_web.domain.study.entity.StudyRecord;
import com.comp.comp_web.domain.study.repository.StudyAttendanceRepository;
import com.comp.comp_web.domain.study.repository.StudyGroupRepository;
import com.comp.comp_web.domain.study.repository.StudyRecordRepository;
import com.comp.comp_web.global.exception.BusinessException;
import com.comp.comp_web.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private static final String DEFAULT_ATTENDANCE_CONTENT = "출석 인증 기록";

    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final StudyAttendanceRepository studyAttendanceRepository;
    private final StudyRecordRepository studyRecordRepository;

    @Value("${study.upload-dir:uploads/study-attendance}")
    private String uploadDir;

    public List<StudyMemberResponse> getAllMembers() {
        return userRepository.findAllByOrderByNameAsc().stream()
            .map(StudyMemberResponse::from)
            .toList();
    }

    @Transactional
    public StudyGroupResponse createStudyGroup(Long loginUserId, CreateStudyGroupRequest request) {
        if (!Objects.equals(loginUserId, request.getLeaderId())) {
            throw new BusinessException(ErrorCode.AUTH_004, "스터디장 본인만 조 정보를 등록할 수 있습니다.");
        }

        List<Long> requestedMemberIds = new ArrayList<>(new LinkedHashSet<>(request.getMemberIds()));
        if (!requestedMemberIds.contains(request.getLeaderId())) {
            requestedMemberIds.add(request.getLeaderId());
        }

        List<User> members = validateAndLoadMembers(requestedMemberIds);

        StudyGroup group = StudyGroup.builder()
            .groupName(request.getGroupName())
            .studyTime(request.getStudyTime())
            .generation(request.getGeneration())
            .memberCount(members.size())
            .build();

        StudyGroup savedGroup = studyGroupRepository.save(group);

        Long groupId = savedGroup.getId().longValue();
        members.forEach(member -> member.updateStudyId(groupId));
        userRepository.saveAll(members);

        return StudyGroupResponse.from(savedGroup);
    }

    @Transactional
    public StudyAttendResponse attend(Long loginUserId, StudyAttendRequest request, MultipartFile attendanceImage) {
        User user = userRepository.findById(loginUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_001));

        Integer groupId = safeStudyGroupId(user.getStudyId());
        StudyGroup group = studyGroupRepository.findById(groupId)
            .orElseThrow(() -> new BusinessException(ErrorCode.STUDY_002));

        String attendanceImageUrl = saveAttendanceImage(attendanceImage, loginUserId, request.getWeekNumber());
        LocalDateTime now = LocalDateTime.now();
        boolean isLate = group.getStudyTime() != null && now.isAfter(group.getStudyTime());

        StudyAttendance attendance = studyAttendanceRepository
            .findByStudentIdAndWeekNumber(loginUserId, request.getWeekNumber())
            .orElseGet(() -> StudyAttendance.builder()
                .studentId(loginUserId)
                .weekNumber(request.getWeekNumber())
                .isAttend(false)
                .late(false)
                .build());

        attendance.markAttendance(request.getAuthCode(), isLate, now);
        StudyAttendance savedAttendance = studyAttendanceRepository.save(attendance);

        StudyRecord record = studyRecordRepository.findByGroupIdAndWeekNumber(groupId, request.getWeekNumber())
            .orElseGet(() -> StudyRecord.builder()
                .groupId(groupId)
                .weekNumber(request.getWeekNumber())
                .content(DEFAULT_ATTENDANCE_CONTENT)
                .build());

        String content = StringUtils.hasText(request.getContent())
            ? request.getContent()
            : record.getContent();

        record.updateAttendanceRecord(now, attendanceImageUrl, content);
        studyRecordRepository.save(record);

        return StudyAttendResponse.builder()
            .userId(loginUserId)
            .groupId(groupId)
            .weekNumber(savedAttendance.getWeekNumber())
            .attend(savedAttendance.getIsAttend())
            .late(savedAttendance.getLate())
            .attendedAt(savedAttendance.getAttendedAt())
            .attendanceImageUrl(attendanceImageUrl)
            .build();
    }

    public StudyGroupAttendanceResponse getGroupAttendance(Integer groupId, Integer weekNumber) {
        StudyGroup group = studyGroupRepository.findById(groupId)
            .orElseThrow(() -> new BusinessException(ErrorCode.STUDY_002));

        List<User> members = userRepository.findAllByStudyId(groupId.longValue());
        List<Long> memberIds = members.stream().map(User::getId).toList();

        int resolvedWeek = resolveWeekNumber(memberIds, weekNumber);
        Map<Long, StudyAttendance> attendanceMap = memberIds.isEmpty()
            ? Map.of()
            : studyAttendanceRepository
                .findAllByStudentIdInAndWeekNumber(memberIds, resolvedWeek)
                .stream()
                .collect(Collectors.toMap(StudyAttendance::getStudentId, attendance -> attendance));

        List<GroupMemberAttendanceStatus> statuses = members.stream()
            .map(member -> {
                StudyAttendance attendance = attendanceMap.get(member.getId());
                return GroupMemberAttendanceStatus.builder()
                    .userId(member.getId())
                    .name(member.getName())
                    .attend(attendance != null && Boolean.TRUE.equals(attendance.getIsAttend()))
                    .late(attendance != null && Boolean.TRUE.equals(attendance.getLate()))
                    .attendedAt(attendance != null ? attendance.getAttendedAt() : null)
                    .build();
            })
            .toList();

        int attendedCount = (int) statuses.stream().filter(GroupMemberAttendanceStatus::getAttend).count();
        int lateCount = (int) statuses.stream().filter(GroupMemberAttendanceStatus::getLate).count();

        return StudyGroupAttendanceResponse.builder()
            .groupId(group.getId())
            .weekNumber(resolvedWeek)
            .totalMembers(members.size())
            .attendedCount(attendedCount)
            .lateCount(lateCount)
            .members(statuses)
            .build();
    }

    @Transactional
    public StudyGroupResponse patchStudy(Long loginUserId, PatchStudyRequest request) {
        if (request.getGroupId() == null) {
            throw new BusinessException(ErrorCode.COMMON_002, "groupId가 필요합니다.");
        }

        StudyGroup group = studyGroupRepository.findById(request.getGroupId())
            .orElseThrow(() -> new BusinessException(ErrorCode.STUDY_002));

        User loginUser = userRepository.findById(loginUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_001));

        if (!Objects.equals(loginUser.getStudyId(), group.getId().longValue())) {
            throw new BusinessException(ErrorCode.AUTH_004, "본인 스터디 그룹만 수정할 수 있습니다.");
        }

        if (StringUtils.hasText(request.getGroupName())) {
            group.updateGroupName(request.getGroupName());
        }

        if (request.getStudyTime() != null) {
            group.updateStudyTime(request.getStudyTime());
        }

        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            List<User> members = validateAndLoadMembers(request.getMemberIds());

            List<User> oldMembers = userRepository.findAllByStudyId(group.getId().longValue());
            oldMembers.forEach(member -> member.updateStudyId(0L));
            userRepository.saveAll(oldMembers);

            Long groupId = group.getId().longValue();
            members.forEach(member -> member.updateStudyId(groupId));
            userRepository.saveAll(members);
            group.updateMemberCount(members.size());
        }

        return StudyGroupResponse.from(group);
    }

    private List<User> validateAndLoadMembers(List<Long> memberIds) {
        List<Long> distinctIds = new ArrayList<>(new LinkedHashSet<>(memberIds));
        if (distinctIds.isEmpty()) {
            throw new BusinessException(ErrorCode.STUDY_006, "스터디원 목록은 비어 있을 수 없습니다.");
        }

        List<User> members = userRepository.findAllById(distinctIds);
        if (members.size() != distinctIds.size()) {
            throw new BusinessException(ErrorCode.STUDY_006, "존재하지 않는 스터디원이 포함되어 있습니다.");
        }
        return members;
    }

    private Integer safeStudyGroupId(Long studyId) {
        if (studyId == null || studyId <= 0) {
            throw new BusinessException(ErrorCode.STUDY_007);
        }
        return Math.toIntExact(studyId);
    }

    private int resolveWeekNumber(List<Long> memberIds, Integer weekNumber) {
        if (weekNumber != null) {
            return weekNumber;
        }

        if (memberIds.isEmpty()) {
            return 1;
        }

        return studyAttendanceRepository.findTopByStudentIdInOrderByWeekNumberDesc(memberIds)
            .map(StudyAttendance::getWeekNumber)
            .orElse(1);
    }

    private String saveAttendanceImage(MultipartFile file, Long userId, Integer weekNumber) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.COMMON_002, "인증 사진 파일이 필요합니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(ErrorCode.FILE_002);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (StringUtils.hasText(originalFilename) && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        String storedName = "study-" + userId + "-w" + weekNumber + "-" + UUID.randomUUID() + extension;

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            Path destinationFile = uploadPath.resolve(storedName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.toString();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_001, e);
        }
    }
}
