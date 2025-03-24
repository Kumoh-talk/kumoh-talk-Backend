package com.example.demo.infra.fake.repository;

import com.example.demo.domain.fake.entity.FakeUserAdditionalInfo;
import com.example.demo.domain.fake.entity.FakeUserInfo;
import com.example.demo.domain.fake.repository.FakeUserRepository;
import com.example.demo.global.oauth.user.OAuth2Provider;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FakeUserRepositoryImpl implements FakeUserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<Long> getByProviderAndProviderId(OAuth2Provider fakeProvider, String fakeProviderId) {
        Optional<User> user = userJpaRepository.findByProviderAndProviderId(fakeProvider, fakeProviderId);
        if (user.isPresent()) {
            return Optional.of(user.get().getId());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Long save(FakeUserInfo fakeUserInfo, FakeUserAdditionalInfo fakeUserAdditionalInfo) {
        User user = User.builder()
                .provider(fakeUserInfo.getFakeProvider())
                .providerId(fakeUserInfo.getFakeProviderId())
                .role(fakeUserInfo.getFakeUserRole())
                .nickname(fakeUserInfo.getFakeUserNickname())
                .build();

        UserAdditionalInfo userAdditionalInfo = UserAdditionalInfo.builder()
                .email(fakeUserAdditionalInfo.getFakeEmail())
                .department(fakeUserAdditionalInfo.getFakeDepartment())
                .studentId(fakeUserAdditionalInfo.getFakeStudentId())
                .grade(fakeUserAdditionalInfo.getFakeGrade())
                .studentStatus(fakeUserAdditionalInfo.getFakeStudentStatus())
                .phoneNumber(fakeUserAdditionalInfo.getFakePhoneNumber())
                .isUpdated(true)
                .build();

        user.mapAdditionalInfo(userAdditionalInfo);
        User saveFakeUser = userJpaRepository.save(user);
        return saveFakeUser.getId();
    }
}
