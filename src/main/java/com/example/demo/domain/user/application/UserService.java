package com.example.demo.domain.user.application;

import com.example.demo.domain.auth.domain.UserPrincipal;
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

    public UserInfoResponse getUserProfile(UserPrincipal user) {
        User savedUser = getUserOrThrow(user.getId());

        return new UserInfoResponse(savedUser);
    }

    @Transactional
    public UserUpdateResponse updateUserProfile(UserPrincipal user, UserUpdateRequest request) {
        User savedUser =  getUserOrThrow(user.getId());

        User userToUpdate = UserUpdateRequest.toUser(request);
        savedUser.updateInfo(userToUpdate);

        return UserUpdateResponse.from(savedUser);
    }

    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.FAIL_USER_LOGIN));
    }

    @Transactional
    public void updateUserPassword(UserPrincipal user, UserPasswordUpdateRequest request) {
        User savedUser = getUserOrThrow(user.getId());

        if(!encoder.matches(request.getOldPassword(), savedUser.getPassword()))
            throw new ServiceException(ErrorCode.INVALID_PASSWORD);

        String encodedNewPassword = encoder.encode(request.getNewPassword());
        savedUser.updatePassword(encodedNewPassword);
    }
}
