package com.example.demo.domain.board.domain.dto.request;


import static com.example.demo.global.regex.S3UrlRegex.*;

import com.example.demo.domain.board.domain.dto.vo.Tag;
import com.example.demo.global.aop.valid.ValidEnum;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardCreateRequest {

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    private String contents;

    @Nullable
    @Size(max = 5,message = "카테고리는 최대 5개까지 가능합니다.")
    private List<String> categoryName;

    @ValidEnum(enumClass = Tag.class,message = "태그는 'SEMINAR', 'NOTICE' 중 하나여야 합니다.")
    @NotNull(message = "태그는 필수 항목입니다.")
    private Tag tag;

    @NotBlank(message = "게시물 대표 이미지는 필수 항목입니다.")
    @Pattern(regexp = S3_BOARD_FILE_URL)
    private String boardHeadImageUrl;
}
