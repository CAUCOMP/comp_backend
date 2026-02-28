package com.comp.comp_web.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    @Column(name = "refreshToken", length = 500)
    private String refreshToken;

    @Column(name = "isVerified")
    private Boolean isVerified;

    @Column(length = 20)
    private String status;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "studyId", nullable = false)
    private Long studyId;


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void verify() {
        this.isVerified = true;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
