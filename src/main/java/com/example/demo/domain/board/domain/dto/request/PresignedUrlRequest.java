package com.example.demo.domain.board.domain.dto.request;

import com.example.demo.domain.board.domain.dto.vo.FileType;
import com.example.demo.global.aop.valid.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PresignedUrlRequest {
	@NotNull(message ="게시글 아이디는 필수입니다.")
	private Long boardId;
	@NotBlank(message ="파일 이름은 필수입니다.")
	private String fileName;
	@NotNull(message ="파일 타입은 필수입니다.")
	@ValidEnum(enumClass = FileType.class)
	private FileType fileType;
}
