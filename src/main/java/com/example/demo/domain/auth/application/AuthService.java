package com.example.demo.domain.auth.application;

import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.response.LoginResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.base.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHandler jwtHandler;

    public void nicknameDuplicateCheck(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new ServiceException(EXIST_SAME_NICKNAME);
        }
    }

    @Transactional
    public void join(JoinRequest request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new ServiceException(EXIST_SAME_USERID);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = request.toEntity(encodedPassword);

        userRepository.save(newUser);

        // TODO. 이메일 전송 추가 필요
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(() -> new ServiceException(
            USERID_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ServiceException(INVALID_PASSWORD);
        }

        JwtUserClaim claim = new JwtUserClaim(user.getId(), user.getRole());
        String token = jwtHandler.createToken(claim);
        return new LoginResponse(token);
    }
}
