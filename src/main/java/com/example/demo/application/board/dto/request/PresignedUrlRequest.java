package com.example.demo.application.board.dto.request;

import com.example.demo.domain.board.service.entity.vo.FileType;
import com.example.demo.global.aop.valid.ValidEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(name = "PresignedUrlRequest", description = "S3 presigned url 요청")
public class PresignedUrlRequest {
	@Schema(description = "게시글 아이디", example = "1")
	@NotNull(message ="게시글 아이디는 필수입니다.")
	private Long boardId;
	@Schema(description = "파일 이름", example = "1.jpg")
	@NotBlank(message ="파일 이름은 필수입니다.")
	private String fileName;
	@Schema(description = "파일 타입", example = "IMAGE")
	@NotNull(message ="파일 타입은 필수입니다.")
	@ValidEnum(enumClass = FileType.class)
	private FileType fileType;
}
