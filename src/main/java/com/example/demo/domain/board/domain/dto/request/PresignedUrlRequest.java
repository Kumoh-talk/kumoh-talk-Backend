package com.example.demo.domain.board.domain.dto.request;

import com.example.demo.domain.board.domain.dto.vo.FileType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PresignedUrlRequest {
	@NotBlank(message ="게시글 아이디는 필수입니다.")
	private Long boardId;
	@NotBlank(message ="파일 이름은 필수입니다.")
	private String fileName;
	@NotBlank(message ="파일 타입은 필수입니다.")
	private FileType fileType;
}
