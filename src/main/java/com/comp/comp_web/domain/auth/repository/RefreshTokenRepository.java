package com.comp.comp_web.domain.auth.repository;

import com.comp.comp_web.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    Optional<RefreshToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    void deleteByExpiresAtBeforeOrRevokedAtIsNotNull(LocalDateTime dateTime);
}
