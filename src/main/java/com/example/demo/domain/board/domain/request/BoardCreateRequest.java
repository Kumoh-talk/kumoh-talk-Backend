package com.example.demo.domain.board.domain.request;


import com.example.demo.domain.board.domain.vo.Tag;
import com.example.demo.domain.board.domain.entity.Board;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class BoardCreateRequest {

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    private String contents;

    //TODO : [Board]카테고리 수 제한 할건지 확인 필요
    @Nullable
    private List<String> categoryName;

    private Tag tag;

    @Builder
    public BoardCreateRequest(String title, String contents, @Nullable List<String> categoryName, Tag tag) {
        this.title = title;
        this.contents = contents;
        this.categoryName = categoryName;
        this.tag = tag;
    }
}
