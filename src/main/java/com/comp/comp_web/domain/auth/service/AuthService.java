package com.comp.comp_web.domain.auth.service;

import com.comp.comp_web.domain.auth.dto.EmailCheckResponse;
import com.comp.comp_web.domain.auth.repository.EmailDatabaseRepository;
import com.comp.comp_web.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final EmailDatabaseRepository emailDatabaseRepository;


    public EmailCheckResponse checkEmail(String email) {
        boolean isValid = emailDatabaseRepository.existsByEmail(email);
        return EmailCheckResponse.builder()
            .isValid(isValid)
            .build();
    }
}
