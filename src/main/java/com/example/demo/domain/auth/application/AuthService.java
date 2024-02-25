package com.example.demo.domain.auth.application;

import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.ValidateEmailRequest;
import com.example.demo.domain.auth.dto.response.LoginResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.token.application.TokenProvider;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.utils.RedisUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

import static com.example.demo.global.base.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final JavaMailSender emailSender;
    private final RedisUtils redisUtils;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public void sendCodeToEmail(@Valid ValidateEmailRequest request) {
        if (!isNotExistEmail(request.getEmail())) {
            throw new ServiceException(EXIST_SAME_EMAIL);
        }
        String authCode = createCode();
        sendEmail(request.getEmail(), authCode);
        // Redis 저장
        redisUtils.setData(request.getEmail(), authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }


    @Transactional
    public void join(JoinRequest request) {
        if (!isNotExistEmail(request.getEmail())) {
            throw new ServiceException(EXIST_SAME_EMAIL);
        }

        String redisAuthCode = redisUtils.getValues(request.getEmail());
        boolean authResult = redisUtils.checkExistsValue(redisAuthCode) && redisAuthCode.equals(request.getAuthCode());

        if(!authResult) {
            throw new ServiceException(MISMATCH_EMAIL_AUTH_CODE);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = request.toEntity(encodedPassword);

        userRepository.save(newUser);
    }

    public boolean isNotExistEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            String accessToken = tokenProvider.createAccessToken(authentication);

            return new LoginResponse(accessToken, "Bearer");
        } catch (Exception e) {
            // 이메일 & 비밀번호를 따로 예외처리를 하면 이메일을 유추할 수 있게되기에 공통 처리
            throw new ServiceException(MISMATCH_EMAIL_OR_PASSWORD);
        }

    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(ErrorCode.NO_SUCH_ALGORITHM);
        }
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
