package com.example.demo.domain.user.service;


import com.example.demo.domain.user.entity.ProfilePresignedUrl;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.implement.UserWriter;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.utils.S3PresignedUrlUtil;
import com.example.demo.global.utils.S3UrlUtil;
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

    public String getPresignedUrl(Long userId, ProfilePresignedUrl request) {
        userReader.validateUser(userId);
        String s3Path = s3UrlUtil.generateProfileS3Path(userId, request.getFileName(), request.getFileType().toString());
        return s3PresignedUrlUtil.generatePresignedUrl(s3Path);

    }

    @Transactional
    public void changeProfileUrl(Long userId, String profileUrl) {
        userReader.validateUser(userId);
        userWriter.changeProfileUrl(userId, profileUrl);
    }

    @Transactional
    public void deleteProfileImage(Long userId) {
        userReader.validateUser(userId);
        userWriter.setDefaultProfileUrl(userId, s3UrlUtil.getDefaultImageUrl());
    }
}
