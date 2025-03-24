package com.example.demo.infra.user_additional_info.repository;

import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserAdditionalInfoJpaRepository extends JpaRepository<UserAdditionalInfo, Long> {
    @Modifying
    @Query("UPDATE UserAdditionalInfo u SET u.isUpdated = false")
    void updateAllIsUpdatedToFalse();
}
