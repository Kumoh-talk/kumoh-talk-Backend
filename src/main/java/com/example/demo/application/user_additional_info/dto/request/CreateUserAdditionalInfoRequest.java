package com.example.demo.application.user_additional_info.dto.request;

import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import com.example.demo.global.aop.valid.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

public record CreateUserAdditionalInfoRequest(
        @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식에 맞춰주세요.") String email,
        @NotBlank(message = "학과는 빈 값일 수 없습니다.") String department,
        @NotNull(message = "학번은 빈 값일 수 없습니다.") int studentId,
        @NotNull(message = "학년은 빈 값일 수 없습니다.") int grade,
        @ValidEnum(enumClass = StudentStatus.class,message = "재학 상태는 휴학, 재학, 졸업 중 하나여야 합니다.") StudentStatus studentStatus,
        @NotBlank(message = "전화번호는 빈 값일 수 없습니다.") String phoneNumber
) {
    public static UserAdditionalInfoData toUserAdditionalInfoData(CreateUserAdditionalInfoRequest request) {
        return UserAdditionalInfoData.builder()
                .email(request.email)
                .department(request.department)
                .studentId(request.studentId)
                .grade(request.grade)
                .studentStatus(StudentStatus.valueOf(request.studentStatus.name()))
                .phoneNumber(request.phoneNumber)
                .build();
    }
}
