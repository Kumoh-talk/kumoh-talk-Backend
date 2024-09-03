package com.example.demo.domain.board.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FileRequest {
	@NotNull(message = "게시물 id는 필수 입니다.")
	private Long boardId;
	@NotBlank(message = "파일 url은 필수 입니다.") //TODO : 파일 url 정규식 validation 추가해야함
	private String url;
}
