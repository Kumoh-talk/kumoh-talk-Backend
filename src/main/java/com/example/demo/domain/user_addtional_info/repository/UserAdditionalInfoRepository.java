package com.example.demo.domain.user_addtional_info.repository;

import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserAdditionalInfoRepository{
    Optional<UserAdditionalInfoData> getUserAdditionalInfoData(Long userId);
    Page<UserAdditionalInfoData> findAll(Pageable pageable);
    void createUserAdditionalInfo(Long userId, UserAdditionalInfoData request);
    void updateAcademicInfo(Long userId, int grade, StudentStatus studentStatus);
}
