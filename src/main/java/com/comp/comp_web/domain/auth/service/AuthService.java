package com.comp.comp_web.domain.auth.service;

import com.comp.comp_web.domain.auth.dto.*;
import com.comp.comp_web.domain.auth.entity.RefreshToken;
import com.comp.comp_web.domain.auth.entity.Role;
import com.comp.comp_web.domain.auth.entity.User;
import com.comp.comp_web.domain.auth.repository.EmailDatabaseRepository;
import com.comp.comp_web.domain.auth.repository.RefreshTokenRepository;
import com.comp.comp_web.domain.auth.repository.UserRepository;
import com.comp.comp_web.global.exception.BusinessException;
import com.comp.comp_web.global.response.ErrorCode;
import com.comp.comp_web.global.util.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    // Refresh Token 유효기간 (14일)
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 14;
    private final UserRepository userRepository;
    private final EmailDatabaseRepository emailDatabaseRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 유효성 체크
     */
    public EmailCheckResponse checkEmail(String email) {
        boolean isValid = emailDatabaseRepository.existsByEmail(email);
        return EmailCheckResponse.builder()
            .isValid(isValid)
            .build();
    }

    /**
     * 회원가입
     */
    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        log.info("회원가입 시도: email={}", request.getEmail());

        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("이미 존재하는 이메일: {}", request.getEmail());
            throw new BusinessException(ErrorCode.AUTH_006, request.getEmail());
        }

        // 2. 비밀번호 암호화 (BCrypt)
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. User 엔티티 생성 (즉시 활성화)
        User user = User.builder()
            .email(request.getEmail())
            .password(encodedPassword)
            .name(request.getName())
            .role(Role.User)
            .studyId(request.getStudyId())
            .isVerified(true)   // 이메일 검증 없이 즉시 인증 완료
            .status("ACTIVE")    // 즉시 활성화 - 바로 로그인 가능
            .createdAt(LocalDateTime.now())
            .build();

        // 4. User 저장
        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: userId={}, email={}", savedUser.getId(), savedUser.getEmail());

        // 5. 토큰 생성 및 저장
        String accessToken = jwtTokenProvider.createAccessToken(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(savedUser.getId());
        saveRefreshToken(savedUser.getId(), refreshToken);

        // 6. 응답 생성
        return AuthResponse.of(accessToken, refreshToken, savedUser);
    }

    /**
     * 로그인
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("로그인 시도: email={}", request.getEmail());

        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> {
                log.warn("존재하지 않는 이메일: {}", request.getEmail());
                return new BusinessException(ErrorCode.AUTH_005);
            });

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("비밀번호 불일치: email={}", request.getEmail());
            throw new BusinessException(ErrorCode.AUTH_005);
        }

        // 3. 계정 상태 확인 (status가 "ACTIVE"가 아니면 비활성화로 간주)
        if (!"ACTIVE".equals(user.getStatus())) {
            log.warn("비활성화된 계정: email={}, status={}", request.getEmail(), user.getStatus());
            throw new BusinessException(ErrorCode.AUTH_009, "관리자에게 문의하세요.");
        }

        // 4. 기존 Refresh Token 무효화
        refreshTokenRepository.deleteByUserId(user.getId());

        // 5. 새 토큰 생성 및 저장
        String accessToken = jwtTokenProvider.createAccessToken(
            user.getId(),
            user.getEmail(),
            user.getRole()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        saveRefreshToken(user.getId(), refreshToken);

        log.info("로그인 성공: userId={}, email={}", user.getId(), user.getEmail());

        // 6. 응답 생성
        return AuthResponse.of(accessToken, refreshToken, user);
    }

    /**
     * 토큰 갱신
     */
    @Transactional
    public TokenResponse refresh(RefreshTokenRequest request) {
        log.info("토큰 갱신 시도");

        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            log.warn("유효하지 않은 Refresh Token");
            throw new BusinessException(ErrorCode.AUTH_008);
        }

        // 2. DB에서 토큰 조회
        String tokenHash = TokenHashUtil.hashToken(request.getRefreshToken());
        RefreshToken refreshToken = refreshTokenRepository
            .findByTokenHash(tokenHash)
            .orElseThrow(() -> {
                log.warn("DB에 존재하지 않는 Refresh Token");
                return new BusinessException(ErrorCode.AUTH_008);
            });

        // 3. 만료 여부 확인
        if (refreshToken.isExpired()) {
            log.warn("만료된 Refresh Token: userId={}", refreshToken.getUserId());
            throw new BusinessException(ErrorCode.AUTH_007, "다시 로그인해주세요.");
        }

        // 4. 폐기 여부 확인
        if (refreshToken.isRevoked()) {
            log.warn("폐기된 Refresh Token: userId={}", refreshToken.getUserId());
            throw new BusinessException(ErrorCode.AUTH_008);
        }

        // 5. 사용자 조회
        User user = userRepository.findById(refreshToken.getUserId())
            .orElseThrow(() -> {
                log.error("사용자를 찾을 수 없음: userId={}", refreshToken.getUserId());
                return new BusinessException(ErrorCode.USER_001);
            });

        // 6. 새 Access Token만 생성 (Refresh Token은 재사용)
        String newAccessToken = jwtTokenProvider.createAccessToken(
            user.getId(),
            user.getEmail(),
            user.getRole()
        );

        log.info("토큰 갱신 완료: userId={} (Refresh Token 재사용)", user.getId());

        // 7. 응답 생성 (기존 Refresh Token 그대로 반환)
        return TokenResponse.of(newAccessToken, request.getRefreshToken());
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(Long userId) {
        log.info("로그아웃 시도: userId={}", userId);

        // 모든 Refresh Token 삭제
        refreshTokenRepository.deleteByUserId(userId);

        log.info("로그아웃 완료: userId={}", userId);
    }

    /**
     * Refresh Token 저장 (해시값으로 저장)
     */
    private void saveRefreshToken(Long userId, String refreshToken) {
        String tokenHash = TokenHashUtil.hashToken(refreshToken);
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAYS);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .userId(userId)
            .tokenHash(tokenHash)
            .expiresAt(expiresAt)
            .build();

        refreshTokenRepository.save(refreshTokenEntity);
        log.debug("Refresh Token 저장 완료: userId={}", userId);
    }
}
