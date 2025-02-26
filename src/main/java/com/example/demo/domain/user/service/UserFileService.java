package com.example.demo.domain.user.service;


import com.example.demo.application.user.dto.request.ChangeProfileUrlRequest;
import com.example.demo.application.user.dto.request.ProfilePresignedUrlRequest;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.implement.UserWriter;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.utils.S3PresignedUrlUtil;
import com.example.demo.global.utils.S3UrlUtil;
import com.example.demo.infra.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserFileService {

    private final UserService userService;
    private final UserReader userReader;
    private final UserWriter userWriter;
    private final S3UrlUtil s3UrlUtil;
    private final S3PresignedUrlUtil s3PresignedUrlUtil;

    public String getPresignedUrl(Long userId, @Valid ProfilePresignedUrlRequest request) {
        UserTarget user = userReader.findUserTarget(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        String s3Path = s3UrlUtil.generateProfileS3Path(user.getUserId(), request.fileType().toString(), request.fileName());
        return s3PresignedUrlUtil.generatePresignedUrl(s3Path);

    }

    @Transactional
    public void changeProfileUrl(Long userId, @Valid ChangeProfileUrlRequest request) {
        UserTarget user = userReader.findUserTarget(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        userWriter.changeProfileUrl(userId, request.url());
    }

    @Transactional
    public void deleteProfileImage(Long userId) {
        UserTarget user = userReader.findUserTarget(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        userWriter.setDefaultProfileUrl(userId, s3UrlUtil.getDefaultImageUrl());
    }
}
