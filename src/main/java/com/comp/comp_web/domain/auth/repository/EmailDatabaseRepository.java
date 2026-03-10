package com.comp.comp_web.domain.auth.repository;

import com.comp.comp_web.domain.auth.entity.EmailDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailDatabaseRepository extends JpaRepository<EmailDatabase, String> {
    boolean existsByEmail(String email);
}
