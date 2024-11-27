package com.example.demo.domain.user_addtional_info.repository;

import com.example.demo.domain.user_addtional_info.domain.UserAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserAdditionalInfoRepository extends JpaRepository<UserAdditionalInfo, Long> {
    @Modifying
    @Query("UPDATE UserAdditionalInfo u SET u.isUpdated = false")
    void updateAllIsUpdatedToFalse();
}
