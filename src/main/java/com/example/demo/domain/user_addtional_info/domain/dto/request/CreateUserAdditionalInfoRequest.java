package com.example.demo.domain.user_addtional_info.domain.dto.request;

import com.example.demo.domain.user_addtional_info.domain.vo.StudentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

public record CreateUserAdditionalInfoRequest(
        @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식에 맞춰주세요.") String email,
        @NotBlank(message = "학과는 빈 값일 수 없습니다.") String department,
        @NotNull(message = "학번은 빈 값일 수 없습니다.") int studentId,
        @NotNull(message = "학년은 빈 값일 수 없습니다.") int grade,
        @NotNull(message = "재적 상태는 빈 값일 수 없습니다.") StudentStatus studentStatus, // TODO. 커스텀 어노테이션으로 수정
        @NotBlank(message = "전화번호는 빈 값일 수 없습니다.") String phoneNumber
) {
}
