package com.example.demo.domain.auth.application;

import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.base.exception.ErrorCode.EXIST_SAME_EMAIL;
import static com.example.demo.global.base.exception.ErrorCode.EXIST_SAME_NAME;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Boolean join(JoinRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = request.toEntity(encodedPassword);

        if (!isNotExistEmail(request.getEmail())) {
            throw new ServiceException(EXIST_SAME_EMAIL);
        }

        return (userRepository.save(newUser).getId()) > 0;
    }

    public boolean isNotExistEmail(String email) {
        return !userRepository.existsByEmail(email);
    }
}
