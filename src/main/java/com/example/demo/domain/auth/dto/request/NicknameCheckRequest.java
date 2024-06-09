package com.example.demo.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.demo.global.regex.UserRegex;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameCheckRequest {
    @NotBlank(message = "닉네임은 빈값 일 수 없습니다.")
    @Pattern(regexp = UserRegex.NICKNAME_REGEXP, message = "닉네임 형식이 맞지 않습니다.")
    private String nickname;
}
