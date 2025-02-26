package com.example.demo.domain.user_addtional_info.repository;

import com.example.demo.application.user_additional_info.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.application.user_additional_info.dto.vo.StudentStatus;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface UserAdditionalInfoRepository{
    Optional<UserAdditionalInfoResponse> getUserAdditionalInfo(Long userId);
    Page<UserAdditionalInfoResponse> findAll(Pageable pageable);
    JwtUserClaim createUserAdditionalInfo(Long userId, CreateUserAdditionalInfoRequest request);
    void updateAcademicInfo(Long userId, int grade, StudentStatus studentStatus);
}
