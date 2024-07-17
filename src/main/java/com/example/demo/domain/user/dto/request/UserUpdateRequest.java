package com.example.demo.domain.user.dto.request;

import com.example.demo.domain.user.domain.User;
import com.example.demo.global.regex.UserRegex;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "닉네임은 빈값 일 수 없습니다.")
    @Pattern(regexp = UserRegex.NICKNAME_REGEXP, message = "닉네임 형식이 맞지 않습니다.")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "전공은 빈값 일 수 없습니다.")
    private String major;

    public static User toUser(UserUpdateRequest request) {
        return User.builder()
                // .name(request.getName())
                // .status(request.getStatus())
                .build();
    }
}
