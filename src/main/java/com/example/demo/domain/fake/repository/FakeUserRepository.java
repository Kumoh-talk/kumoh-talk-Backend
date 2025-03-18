package com.example.demo.domain.fake.repository;

import com.example.demo.domain.fake.entity.FakeUserAdditionalInfo;
import com.example.demo.domain.fake.entity.FakeUserInfo;
import com.example.demo.global.oauth.user.OAuth2Provider;

import java.util.Optional;

public interface FakeUserRepository {
    Optional<Long> getByProviderAndProviderId(OAuth2Provider fakeProvider, String fakeProviderId);

    Long save(FakeUserInfo fakeUserInfo, FakeUserAdditionalInfo fakeUserAdditionalInfo);
}
