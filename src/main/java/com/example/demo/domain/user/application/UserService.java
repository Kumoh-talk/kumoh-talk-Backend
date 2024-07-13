package com.example.demo.domain.user.application;

import com.example.demo.domain.user.domain.User;
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

    public UserInfoResponse getUserProfile(Long userId) {
        User savedUser = getUserOrThrow(userId);

        return new UserInfoResponse(savedUser);
    }

    @Transactional
    public UserUpdateResponse updateUserProfile(Long userId, UserUpdateRequest request) {
        User savedUser =  getUserOrThrow(userId);

        User userToUpdate = UserUpdateRequest.toUser(request);
        savedUser.updateInfo(userToUpdate);

        return UserUpdateResponse.from(savedUser);
    }

    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateUserPassword(Long userId, UserPasswordUpdateRequest request) {
        User savedUser = getUserOrThrow(userId);

//        if(!encoder.matches(request.getOldPassword(), savedUser.getPassword()))
//            throw new ServiceException(ErrorCode.INVALID_PASSWORD);
//
        String encodedNewPassword = encoder.encode(request.getNewPassword());
//        savedUser.updatePassword(encodedNewPassword);
    }
}
