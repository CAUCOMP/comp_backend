package com.comp.comp_web.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "RefreshToken")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(length = 255)
    private String id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "tokenHash", nullable = false, length = 255)
    private String tokenHash;

    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revokedAt")
    private LocalDateTime revokedAt;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public RefreshToken(Long userId, String tokenHash, LocalDateTime expiresAt) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public void revoke() {
        this.revokedAt = LocalDateTime.now();
    }
}
