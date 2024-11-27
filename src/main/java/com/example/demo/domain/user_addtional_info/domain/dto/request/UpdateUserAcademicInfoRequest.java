package com.example.demo.domain.user_addtional_info.domain.dto.request;

import com.example.demo.domain.user_addtional_info.domain.vo.StudentStatus;
import com.example.demo.global.aop.valid.ValidEnum;
import jakarta.validation.constraints.NotNull;

public record UpdateUserAcademicInfoRequest(
        @NotNull(message = "학년은 빈 값일 수 없습니다.") int grade,
        @ValidEnum(enumClass = StudentStatus.class,message = "재학 상태는 휴학, 재학, 좋업 중 하나여야 합니다.") StudentStatus studentStatus
) {
}
