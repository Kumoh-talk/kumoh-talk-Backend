package com.example.demo.domain.user.domain.dto.request;

import com.example.demo.application.board.dto.vo.FileType;
import com.example.demo.global.aop.valid.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfilePresignedUrlRequest(
        @NotBlank(message = "파일 이름은 필수입니다.")
        String fileName,

        @NotNull(message = "파일 타입은 필수입니다.")
        @ValidEnum(enumClass = FileType.class)
        FileType fileType
) {
}
