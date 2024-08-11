package com.example.demo.domain.user.application;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.dto.request.CompleteRegistrationRequest;
import com.example.demo.domain.user.dto.request.UserPasswordUpdateRequest;
import com.example.demo.domain.user.dto.request.UserUpdateRequest;
import com.example.demo.domain.user.dto.response.UserInfoResponse;
import com.example.demo.domain.user.dto.response.UserUpdateResponse;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public void completeRegistration(Long userId, CompleteRegistrationRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        if(userRepository.existsByNickname(request.nickname()))
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);

        user.setInitialInfo(request);
    }

//    @Transactional
//    public void updateUserProfile(Long userId, UserUpdateRequest request) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//
//        User userToUpdate = UserUpdateRequest.toUser(request);
//        savedUser.updateInfo(userToUpdate);
//
//        return UserUpdateResponse.from(savedUser);
//    }
}
