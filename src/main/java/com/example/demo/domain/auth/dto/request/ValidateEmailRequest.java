package com.example.demo.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateEmailRequest {
    @NotBlank(message = "이메일은 빈값 일 수 없습니다.")
    @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식이 맞지 않습니다.")
    private String email;
}
