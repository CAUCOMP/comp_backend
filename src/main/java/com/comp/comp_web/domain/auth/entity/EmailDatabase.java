package com.comp.comp_web.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EmailDatabase")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailDatabase {

    @Id
    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String name;

    @Builder
    public EmailDatabase(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
