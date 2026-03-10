package com.comp.comp_web.global.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class TokenHashUtil {

    private static final String ALGORITHM = "SHA-256";

    private TokenHashUtil() {
        // 유틸리티 클래스는 인스턴스화 방지
    }

    /**
     * Refresh Token을 SHA-256으로 해시
     * @param token 원본 토큰
     * @return 해시된 토큰 (16진수 문자열)
     */
    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("해시 알고리즘을 찾을 수 없습니다: {}", ALGORITHM, e);
            throw new RuntimeException("토큰 해시 생성 실패", e);
        }
    }

    /**
     * 바이트 배열을 16진수 문자열로 변환
     * @param bytes 바이트 배열
     * @return 16진수 문자열
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
