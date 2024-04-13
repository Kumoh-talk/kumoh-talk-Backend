package com.example.demo.domain.auth.application;

import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.ValidateEmailRequest;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.utils.RedisUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

import static com.example.demo.global.base.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;

    // TODO. 인증코드가 아닌 링크식 이메일 인증 구현
    public void sendCodeToEmail(@Valid ValidateEmailRequest request) {
        if (isNotExistEmail(request.getEmail())) {
            throw new ServiceException(EXIST_SAME_EMAIL);
        }
//        String authCode = createCode();
//        sendEmail(request.getEmail(), authCode);
//        // Redis 저장
//        redisUtils.setData(request.getEmail(), authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }


    @Transactional
    public void join(JoinRequest request) {
        if (isNotExistEmail(request.getEmail())) {
            throw new ServiceException(EXIST_SAME_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = request.toEntity(encodedPassword);

        userRepository.save(newUser);
    }

    public boolean isNotExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void sendEmail(String toEmail, String authCode) {
        SimpleMailMessage emailForm = new SimpleMailMessage();
        emailForm.setTo(toEmail);
        emailForm.setSubject("[LikeLion-Kit] 회원가입 인증 이메일 입니다.");
        emailForm.setText("인증 코드는 " + authCode + " 입니다." +
                "\n" +
                "해당 인증 코드를 인증 코드 확인란에 기입하여 주세요.");

        try{
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new ServiceException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }
}
