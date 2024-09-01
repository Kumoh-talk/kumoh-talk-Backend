package com.example.demo.domain.board.domain.dto.request;


import com.example.demo.domain.board.domain.dto.vo.Tag;
import com.example.demo.global.aop.valid.ValidEnum;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Size(max = 5,message = "카테고리는 최대 3개까지 가능합니다.")
    private List<String> categoryName;

    @ValidEnum(enumClass = Tag.class,message = "태그는 seminar, notice 중 하나여야 합니다.")
    private Tag tag;

    @Builder
    public BoardCreateRequest(String title, String contents, @Nullable List<String> categoryName, Tag tag) {
        this.title = title;
        this.contents = contents;
        this.categoryName = categoryName;
        this.tag = tag;
    }

    public boolean isSeminarBoard() {
        return tag.equals(Tag.seminar);
    }
}
