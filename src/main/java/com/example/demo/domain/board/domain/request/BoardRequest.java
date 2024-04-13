package com.example.demo.domain.board.domain.request;


import com.example.demo.domain.board.domain.BoardStatus;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.user.domain.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BoardRequest {

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    @Size(max = 500,message = "최대 제한 500글자 입니다.") // TODO : 게시판 글자수 수정해야함
    private String contents;

    //TODO : 카테고리 수 제한 할건지 확인 필요
    @Nullable
    private List<String> categoryName;

    @Builder
    public BoardRequest(String title, String contents, @Nullable List<String> categoryName) {
        this.title = title;
        this.contents = contents;
        this.categoryName = categoryName;
    }

    public static Board toEntity(BoardRequest boardRequest) {
        return Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContents())
                .status(BoardStatus.FAKE) // 첫 저장은 임시 게시물이기 때문
                .build();
    }
}
