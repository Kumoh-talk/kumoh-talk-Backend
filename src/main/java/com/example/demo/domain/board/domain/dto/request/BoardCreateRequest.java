package com.example.demo.domain.board.domain.dto.request;


import static com.example.demo.global.regex.S3UrlRegex.*;

import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.global.aop.valid.ValidEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "게시물 생성 요청")
public class BoardCreateRequest {

    @Schema(description = "게시물 제목", example = "게시물 제목")
    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 50,message = "최대 제한 45글자 입니다.")
    private String title;

    @Schema(description = "게시물 내용", example = "게시물 내용")
    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    private String contents;

    @Schema(description = "게시물 카테고리 이름 리스트", example = "{'카테고리1','카테고리2'}")
    @Nullable
    @Size(max = 5,message = "카테고리는 최대 5개까지 가능합니다.")
    private List<String> categoryName;

    @Schema(description = "게시물 태그", example = "SEMINAR")
    @ValidEnum(enumClass = BoardType.class,message = "태그는 'SEMINAR', 'NOTICE' 중 하나여야 합니다.")
    @NotNull(message = "태그는 필수 항목입니다.")
    private BoardType boardType;

    @Schema(description = "게시물 대표 이미지 URL", example = "https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/")
    @NotBlank(message = "게시물 대표 이미지는 필수 항목입니다.")
    @Pattern(regexp = S3_BOARD_FILE_URL)
    private String boardHeadImageUrl;
}
