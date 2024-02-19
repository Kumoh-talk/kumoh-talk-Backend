package com.example.demo.domain.auth.dto.request;

import com.example.demo.domain.user.domain.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.demo.global.regex.UserRegex.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    @NotBlank(message = "이름은 빈값 일 수 없습니다.")
    @Pattern(regexp = NAME_REGEXP, message = "이름 형식이 맞지 않습니다.")
    private String name;

    @NotBlank(message = "이메일은 빈값 일 수 없습니다.")
    @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 빈값 일 수 없습니다.")
    @Pattern(regexp = PASSWORD_REGEXP, message = "비밀번호 형식이 맞지 않습니다.")
    private String password;

    @Enumerated(EnumType.STRING)
    private User.Track track;

    @NotBlank(message = "전공은 빈값 일 수 없습니다.")
    private String major;

    public User toEntity(String password) {
        return new User(this.name, this.email, password, this.track,this.major);
    }
}
