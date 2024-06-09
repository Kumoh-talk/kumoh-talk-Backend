package com.example.demo.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import com.example.demo.global.regex.UserRegex;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdateRequest {
    @NotBlank(message = "비밀번호는 빈값 일 수 없습니다.")
    @Pattern(regexp = UserRegex.PASSWORD_REGEXP, message = "비밀번호 형식이 맞지 않습니다.")
    private String oldPassword;

    @NotBlank(message = "비밀번호는 빈값 일 수 없습니다.")
    @Pattern(regexp = UserRegex.PASSWORD_REGEXP, message = "비밀번호 형식이 맞지 않습니다.")
    private String newPassword;
}
