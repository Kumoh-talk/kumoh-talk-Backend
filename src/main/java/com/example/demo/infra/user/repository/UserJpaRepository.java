package com.example.demo.infra.user.repository;

import com.example.demo.domain.user.vo.Role;
import com.example.demo.global.oauth.user.OAuth2Provider;
import com.example.demo.infra.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(OAuth2Provider provider, String providerId);

    boolean existsByNickname(String nickname);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.nickname = :nickname WHERE u.id = :userId")
    void updateNickName(@Param("userId") Long userId, @Param("nickname") String nickname);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.profileImageUrl = :profileImageUrl WHERE u.id = :userId")
    void changeProfileUrl(@Param("userId") Long userId, @Param("profileImageUrl") String profileUrl);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.nickname = :nickname, u.name = :name, u.profileImageUrl = :profileImageUrl, u.role = :role WHERE u.id = :userId")
    void updateUserInfo(@Param("userId") Long userId,
                        @Param("nickname") String nickname,
                        @Param("name") String name,
                        @Param("profileImageUrl") String profileImageUrl,
                        @Param("role") Role role);
}
