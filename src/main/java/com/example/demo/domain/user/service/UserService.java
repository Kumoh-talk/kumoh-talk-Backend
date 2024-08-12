package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.dto.request.CompleteRegistrationRequest;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public void checkNicknameDuplicate(String nickname) {
        if(userRepository.existsByNickname(nickname)){
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);
        }
    }

    @Transactional
    public void completeRegistration(Long userId, CompleteRegistrationRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        if(userRepository.existsByNickname(request.nickname())){
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);
        }
        user.setInitialInfo(request.nickname());
    }
}
