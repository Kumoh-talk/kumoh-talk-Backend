package com.example.demo.domain.board.domain.dto.request;

import static com.example.demo.global.regex.S3UrlRegex.*;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.global.aop.valid.ValidEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Schema(name = "BoardUpdateRequest",description = "게시물 수정 요청")
public class BoardUpdateRequest {

    @Schema(description = "게시물 ID", example = "1")
    @NotNull(message = "게시물 번호는 필수 항목입니다.")
    private Long id;

    @Schema(description = "게시물 제목", example = "게시물 제목")
    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 45, message = "최대 제한 45글자 입니다.")
    private String title;

    @Schema(description = "게시물 내용", example = "게시물 내용")
    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    private String contents;

    @Schema(description = "게시물 카테고리", example = "['카테고리1','카테고리2']")
    @Nullable
    @Size(max = 5, message = "카테고리는 최대 5개까지 가능합니다.")
    private List<String> categoryName;

    @Schema(description = "게시물 상태 [DRAFT/PUBLISHED]", example = "DRAFT")
    @ValidEnum(enumClass = Status.class, message = "게시물 상태는 DRAFT, PUBLISHED 중 하나여야 합니다.")
    private Status status;

    @Schema(description = "게시물 대표 이미지 URL", example = "https://s3.bucket/board/1.jpg")
    @NotBlank(message = "게시물 대표 이미지는 필수 항목입니다.")
    @Pattern(regexp = S3_BOARD_FILE_URL)
    private String boardHeadImageUrl;
}
