package com.comp.comp_web.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 인증 관련 에러 (AUTH_xxx)
    AUTH_001("AUTH_001", "인증 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_002("AUTH_002", "인증 토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_003("AUTH_003", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    AUTH_004("AUTH_004", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    AUTH_005("AUTH_005", "이메일 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_006("AUTH_006", "이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    AUTH_007("AUTH_007", "리프레시 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_008("AUTH_008", "리프레시 토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_009("AUTH_009", "비활성화된 계정입니다.", HttpStatus.FORBIDDEN),

    // 사용자 관련 에러 (USER_xxx)
    USER_001("USER_001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_002("USER_002", "이메일 인증이 필요합니다.", HttpStatus.FORBIDDEN),
    USER_003("USER_003", "가입 승인 대기 중입니다.", HttpStatus.FORBIDDEN),
    USER_004("USER_004", "사용자 정보 수정에 실패했습니다.", HttpStatus.BAD_REQUEST),

    // 스터디 관련 에러 (STUDY_xxx)
    STUDY_001("STUDY_001", "스터디를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    STUDY_002("STUDY_002", "스터디 그룹을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    STUDY_003("STUDY_003", "스터디 출석 처리에 실패했습니다.", HttpStatus.BAD_REQUEST),
    STUDY_004("STUDY_004", "잘못된 인증 코드입니다.", HttpStatus.BAD_REQUEST),
    STUDY_005("STUDY_005", "과제 제출 기한이 지났습니다.", HttpStatus.BAD_REQUEST),

    // 프로젝트 관련 에러 (PROJECT_xxx)
    PROJECT_001("PROJECT_001", "프로젝트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PROJECT_002("PROJECT_002", "프로젝트 생성에 실패했습니다.", HttpStatus.BAD_REQUEST),

    // 모집 관련 에러 (RECRUIT_xxx)
    RECRUIT_001("RECRUIT_001", "모집 기간이 아닙니다.", HttpStatus.BAD_REQUEST),
    RECRUIT_002("RECRUIT_002", "이미 지원한 내역이 있습니다.", HttpStatus.CONFLICT),
    RECRUIT_003("RECRUIT_003", "모집 인원이 마감되었습니다.", HttpStatus.BAD_REQUEST),

    // 파일 관련 에러 (FILE_xxx)
    FILE_001("FILE_001", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_002("FILE_002", "지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST),
    FILE_003("FILE_003", "파일 크기가 너무 큽니다.", HttpStatus.BAD_REQUEST),

    // 공통 에러 (COMMON_xxx)
    COMMON_001("COMMON_001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    COMMON_002("COMMON_002", "필수 파라미터가 누락되었습니다.", HttpStatus.BAD_REQUEST),
    COMMON_003("COMMON_003", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    COMMON_004("COMMON_004", "데이터베이스 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    COMMON_005("COMMON_005", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COMMON_006("COMMON_006", "입력값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 검증 관련 에러 (VALID_xxx)
    VALID_001("VALID_001", "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    VALID_002("VALID_002", "비밀번호는 8자 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    VALID_003("VALID_003", "이름은 2자 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    VALID_004("VALID_004", "필수 입력값이 누락되었습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    /**
     * HTTP 상태 코드를 반환합니다.
     * null인 경우 BAD_REQUEST를 기본값으로 사용합니다.
     *
     * @return HTTP 상태 코드
     */
    public HttpStatus getHttpStatus() {
        return httpStatus != null ? httpStatus : HttpStatus.BAD_REQUEST;
    }
}
