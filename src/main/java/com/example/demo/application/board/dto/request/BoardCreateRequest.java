package com.example.demo.application.board.dto.request;


import static com.example.demo.global.regex.S3UrlRegex.*;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.global.aop.valid.ValidEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(description = "게시물 생성 요청")
public record BoardCreateRequest (

    @Schema(description = "게시물 제목", example = "게시물 제목")
    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 50,message = "최대 제한 45글자 입니다.")
    String title,

    @Schema(description = "게시물 내용", example = "게시물 내용")
    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    String contents,

    @Schema(
        description = "게시물 카테고리 이름 리스트",
        example = "[\"카테고리1\", \"카테고리2\"]"
    )
    @NotNull
    @Size(max = 5,message = "카테고리는 최대 5개까지 가능합니다.")
    List<String> categoryName,

    @Schema(description = "게시물 태그", example = "SEMINAR")
    @ValidEnum(enumClass = BoardType.class,message = "태그는 'SEMINAR', 'NOTICE' 중 하나여야 합니다.")
    @NotNull(message = "태그는 필수 항목입니다.")
    BoardType boardType,

    @Schema(description = "게시물 대표 이미지 URL", example = "https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/board/15/image/54599f59-1d5b-4167-b9f7-96f84d3c452d/example.jpg")
    @Pattern(regexp = S3_BOARD_FILE_URL)
    String boardHeadImageUrl
){
    public BoardContent toBoardContent(){
        return new BoardContent(
            this.title,
            this.contents,
            this.boardType,
            this.boardHeadImageUrl,
            Status.DRAFT
        );
    }

    public BoardCategoryNames toBoardCategoryNames() {
        return new BoardCategoryNames(
            this.categoryName
        );
    }
}
