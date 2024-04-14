package com.example.demo.domain.auth.dto.request;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user.domain.vo.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.demo.global.regex.UserRegex.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    @NotBlank(message = "이름은 빈값 일 수 없습니다.")
//    @Pattern(regexp = NAME_REGEXP, message = "이름 형식이 맞지 않습니다.")
    private String name;

    @NotBlank(message = "이메일은 빈값 일 수 없습니다.")
//    @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임은 빈값 일 수 없습니다.")
    @Size(min = 2, max = 20, message = "닉네임은 적어도 최소 2글자, 최대 20글자입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 빈값 일 수 없습니다.")
//    @Pattern(regexp = PASSWORD_REGEXP, message = "비밀번호 형식이 맞지 않습니다.")
    private String password;

    @NotBlank(message = "학과는 빈값 일 수 없습니다.")
    private String department;

    @Enumerated(EnumType.STRING)
    private Status status;

    @NotBlank(message = "희망 분야는 빈값 일 수 없습니다.")
    private String field;

    public User toEntity(String hashedPassword) {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .nickname(this.nickname)
                .password(hashedPassword)
                .role(Role.ROLE_USER)
                .department(this.department)
                .status(this.status)
                .field(this.field)
                .build();
    }
}
