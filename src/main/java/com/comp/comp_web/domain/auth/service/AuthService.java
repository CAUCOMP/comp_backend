package com.comp.comp_web.domain.auth.service;

import com.comp.comp_web.domain.auth.dto.EmailCheckResponse;
import com.comp.comp_web.domain.auth.dto.UserResponse;
import com.comp.comp_web.domain.auth.entity.User;
import com.comp.comp_web.domain.auth.repository.EmailDatabaseRepository;
import com.comp.comp_web.domain.auth.repository.UserRepository;
import com.comp.comp_web.global.exception.BusinessException;
import com.comp.comp_web.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final EmailDatabaseRepository emailDatabaseRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_001, "사용자 ID: " + id));
        return UserResponse.from(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_001, "이메일: " + email));
        return UserResponse.from(user);
    }

    public EmailCheckResponse checkEmail(String email) {
        boolean isValid = emailDatabaseRepository.existsByEmail(email);
        return EmailCheckResponse.builder()
            .isValid(isValid)
            .build();
    }
}
