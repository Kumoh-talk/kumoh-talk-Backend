package com.example.demo.domain.fake.implement;

import com.example.demo.domain.fake.entity.FakeUserAdditionalInfo;
import com.example.demo.domain.fake.entity.FakeUserInfo;
import com.example.demo.domain.fake.repository.FakeUserRepository;
import com.example.demo.global.oauth.user.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FakeUserHandler {
    private final FakeUserRepository fakeUserRepository;

    public Optional<Long> getByProviderAndProviderId(OAuth2Provider fakeProvider, String fakeProviderId) {
        return fakeUserRepository.getByProviderAndProviderId(fakeProvider, fakeProviderId);
    }

    public Long save(FakeUserInfo fakeUserInfo, FakeUserAdditionalInfo fakeUserAdditionalInfo) {
        return fakeUserRepository.save(fakeUserInfo, fakeUserAdditionalInfo);
    }
}
