package com.example.demo.domain.auth.dto.request;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.regex.UserRegex;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "이메일은 빈값 일 수 없습니다.")
    @Pattern(regexp = UserRegex.EMAIL_REGEXP, message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임은 빈값 일 수 없습니다.")
    @Pattern(regexp = UserRegex.NICKNAME_REGEXP, message = "닉네임 형식이 맞지 않습니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 빈값 일 수 없습니다.")
    @Pattern(regexp = UserRegex.PASSWORD_REGEXP, message = "비밀번호 형식이 맞지 않습니다.")
    private String password;

    @NotBlank(message = "학과는 빈값 일 수 없습니다.")
    private String department;

    @NotBlank(message = "희망 분야는 빈값 일 수 없습니다.")
    private String field;

    public User toEntity(String hashedPassword) {
        return User.builder()
                .email(this.email)
                .nickname(this.nickname)
                .password(hashedPassword)
                .role(Role.ROLE_USER)
                .department(this.department)
                .field(this.field)
                .build();
    }
}
