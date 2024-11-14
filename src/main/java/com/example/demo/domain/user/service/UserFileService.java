package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.dto.request.ChangeProfileUrlRequest;
import com.example.demo.domain.user.domain.dto.request.ProfilePresignedUrlRequest;
import com.example.demo.global.utils.S3PresignedUrlUtil;
import com.example.demo.global.utils.S3UrlUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserFileService {

    private final UserService userService;
    private final S3UrlUtil s3UrlUtil;
    private final S3PresignedUrlUtil s3PresignedUrlUtil;

    public String getPresignedUrl(Long userId, @Valid ProfilePresignedUrlRequest request) {
        User user = userService.validateUser(userId);
        String s3Path = s3UrlUtil.generateProfileS3Path(user.getId(), request.fileType().toString(), request.fileName());
        return s3PresignedUrlUtil.generatePresignedUrl(s3Path);
    }

    @Transactional
    public void changeProfileUrl(Long userId, @Valid ChangeProfileUrlRequest request) {
        User user = userService.validateUser(userId);
        user.changeProfileUrl(request.url());
    }

    @Transactional
    public void deleteProfileImage(Long userId) {
        User user = userService.validateUser(userId);
        user.setDefaultProfileUrl(s3UrlUtil.getDefaultImageUrl());
    }
}
