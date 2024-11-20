package com.example.demo.domain.board.domain.dto.request;

import static com.example.demo.global.regex.S3UrlRegex.*;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@Schema(name = "BoardUpdateRequest",description = "게시물 수정 요청")
public class BoardUpdateRequest {

    @Schema(description = "게시물 ID", example = "1")
    @NotNull(message = "게시물 번호는 필수 항목입니다.")
    private Long id;

    @Schema(description = "게시물 제목", example = "게시물 제목")
    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 30,message = "최대 제한 45글자 입니다.")
    private String title;

    @Schema(description = "게시물 내용", example = "게시물 내용")
    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    private String contents;

    @Schema(
        description = "게시물 카테고리 이름 리스트",
        example = "[\"카테고리1\", \"카테고리2\"]"
    )
    @Nullable
    private List<String> categoryName;

    @Schema(description = "게시 여부", example = "true")
    @NotNull(message = "게시 여부는 필수 항목입니다.")
    private Boolean isPublished;

    @Schema(description = "게시물 대표 이미지 URL", example = "https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/board/15/image/54599f59-1d5b-4167-b9f7-96f84d3c452d/example.jpg")
    @NotBlank(message = "게시물 대표 이미지는 필수 항목입니다.")
    @Pattern(regexp = S3_BOARD_FILE_URL)
    private String boardHeadImageUrl;
}
