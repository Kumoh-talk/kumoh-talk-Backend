package com.example.demo.domain.fake.service;

import com.example.demo.domain.fake.entity.FakeUserAdditionalInfo;
import com.example.demo.domain.fake.entity.FakeUserInfo;
import com.example.demo.domain.fake.implement.FakeUserHandler;
import com.example.demo.domain.token.entity.Token;
import com.example.demo.domain.user.vo.Role;
import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.global.oauth.user.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FakeAuthService {
    private volatile Long fakeUserId = null;

    private final JwtHandler jwtHandler;

    private final FakeUserHandler fakeUserHandler;

    private final OAuth2Provider fakeProvider = OAuth2Provider.GOOGLE;
    private final String fakeProviderId = "fake-provider-id";
    private final Role fakeUserRole = Role.ROLE_ADMIN;
    private final String fakeUserNickname = "fake-admin-nickname";

    private final String fakeUserEmail = "fakeEmail@";
    private final String fakeUserDepartment = "Computer Science";
    private final int fakeUserStudentId = 20000000;
    private final int fakeUserGrade = 4;
    private final StudentStatus fakeUserStudentStatus = StudentStatus.ENROLLED;
    private final String fakeUserPhoneNumber = "01000000000";


    public Token fakeLogin() {
        if (fakeUserId == null) {
            fakeUserHandler.getByProviderAndProviderId(fakeProvider, fakeProviderId).ifPresent(id -> fakeUserId = id);

            if (fakeUserId == null) {
                FakeUserInfo fakeUserInfo = FakeUserInfo.builder()
                        .fakeProvider(fakeProvider)
                        .fakeProviderId(fakeProviderId)
                        .fakeUserRole(fakeUserRole)
                        .fakeUserNickname(fakeUserNickname)
                        .build();
                FakeUserAdditionalInfo fakeUserAdditionalInfo = FakeUserAdditionalInfo.builder()
                        .fakeEmail(fakeUserEmail)
                        .fakeDepartment(fakeUserDepartment)
                        .fakeStudentId(fakeUserStudentId)
                        .fakeGrade(fakeUserGrade)
                        .fakeStudentStatus(fakeUserStudentStatus)
                        .fakePhoneNumber(fakeUserPhoneNumber)
                        .build();
                fakeUserId = fakeUserHandler.save(fakeUserInfo, fakeUserAdditionalInfo);
            }
        }
        JwtUserClaim claim = new JwtUserClaim(fakeUserId, fakeUserRole);
        return jwtHandler.createTokens(claim);
    }
}
