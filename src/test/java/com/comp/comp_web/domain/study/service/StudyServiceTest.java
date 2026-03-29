package com.comp.comp_web.domain.study.service;

import com.comp.comp_web.domain.auth.entity.User;
import com.comp.comp_web.domain.auth.repository.UserRepository;
import com.comp.comp_web.domain.study.dto.request.CreateStudyGroupRequest;
import com.comp.comp_web.domain.study.dto.response.StudyGroupAttendanceResponse;
import com.comp.comp_web.domain.study.entity.StudyAttendance;
import com.comp.comp_web.domain.study.entity.StudyGroup;
import com.comp.comp_web.domain.study.repository.StudyAttendanceRepository;
import com.comp.comp_web.domain.study.repository.StudyGroupRepository;
import com.comp.comp_web.domain.study.repository.StudyRecordRepository;
import com.comp.comp_web.global.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @Mock
    private StudyAttendanceRepository studyAttendanceRepository;

    @Mock
    private StudyRecordRepository studyRecordRepository;

    @InjectMocks
    private StudyService studyService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(studyService, "uploadDir", "build/test-uploads/study-attendance");
    }

    @Test
    void createStudyGroup_throwsWhenLeaderIsNotRequester() {
        CreateStudyGroupRequest request = new CreateStudyGroupRequest();
        ReflectionTestUtils.setField(request, "groupName", "A조");
        ReflectionTestUtils.setField(request, "studyTime", LocalDateTime.now().plusDays(1));
        ReflectionTestUtils.setField(request, "generation", 39);
        ReflectionTestUtils.setField(request, "leaderId", 2L);
        ReflectionTestUtils.setField(request, "memberIds", List.of(2L, 3L));

        assertThrows(BusinessException.class, () -> studyService.createStudyGroup(1L, request));
    }

    @Test
    void getGroupAttendance_usesLatestWeekWhenWeekIsNull() {
        StudyGroup group = StudyGroup.builder()
            .groupName("A조")
            .generation(39)
            .memberCount(2)
            .studyTime(LocalDateTime.now())
            .build();
        ReflectionTestUtils.setField(group, "id", 10);

        User user1 = User.builder().id(1L).name("Kim").email("a@a.com").password("p").studyId(10L).build();
        User user2 = User.builder().id(2L).name("Lee").email("b@b.com").password("p").studyId(10L).build();

        StudyAttendance latest = StudyAttendance.builder()
            .studentId(1L)
            .weekNumber(4)
            .isAttend(true)
            .late(false)
            .build();

        StudyAttendance attend1 = StudyAttendance.builder()
            .studentId(1L)
            .weekNumber(4)
            .isAttend(true)
            .late(true)
            .attendedAt(LocalDateTime.now())
            .build();

        when(studyGroupRepository.findById(10)).thenReturn(Optional.of(group));
        when(userRepository.findAllByStudyId(10L)).thenReturn(List.of(user1, user2));
        when(studyAttendanceRepository.findTopByStudentIdInOrderByWeekNumberDesc(List.of(1L, 2L)))
            .thenReturn(Optional.of(latest));
        when(studyAttendanceRepository.findAllByStudentIdInAndWeekNumber(List.of(1L, 2L), 4))
            .thenReturn(List.of(attend1));

        StudyGroupAttendanceResponse response = studyService.getGroupAttendance(10, null);

        assertEquals(4, response.getWeekNumber());
        assertEquals(2, response.getTotalMembers());
        assertEquals(1, response.getAttendedCount());
        assertEquals(1, response.getLateCount());
        assertNotNull(response.getMembers());
        assertEquals(2, response.getMembers().size());
    }

    @Test
    void createStudyGroup_savesGroupAndReturnsResponse() {
        CreateStudyGroupRequest request = new CreateStudyGroupRequest();
        ReflectionTestUtils.setField(request, "groupName", "A조");
        ReflectionTestUtils.setField(request, "studyTime", LocalDateTime.now().plusDays(1));
        ReflectionTestUtils.setField(request, "generation", 39);
        ReflectionTestUtils.setField(request, "leaderId", 1L);
        ReflectionTestUtils.setField(request, "memberIds", List.of(1L, 2L));

        User user1 = User.builder().id(1L).name("Kim").email("a@a.com").password("p").studyId(0L).build();
        User user2 = User.builder().id(2L).name("Lee").email("b@b.com").password("p").studyId(0L).build();

        StudyGroup savedGroup = StudyGroup.builder()
            .groupName("A조")
            .generation(39)
            .memberCount(2)
            .studyTime(LocalDateTime.now().plusDays(1))
            .build();
        ReflectionTestUtils.setField(savedGroup, "id", 7);

        when(userRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(user1, user2));
        when(studyGroupRepository.save(any(StudyGroup.class))).thenReturn(savedGroup);

        assertEquals(7, studyService.createStudyGroup(1L, request).getGroupId());
    }
}
