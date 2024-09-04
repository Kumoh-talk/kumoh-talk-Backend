package com.example.demo.domain.board.domain.dto.request;

import static com.example.demo.global.regex.S3UrlRegex.*;

import com.example.demo.global.regex.S3UrlRegex;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class FileRequest {
	@NotNull(message = "게시물 id는 필수 입니다.")
	private Long boardId;
	@NotBlank(message = "파일 url은 필수 입니다.")
	@Pattern(regexp = S3_BOARD_FILE_URL)
	private String url;
}
