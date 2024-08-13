package com.example.demo.domain.board.domain.dto.request;

import com.example.demo.domain.board.domain.dto.vo.Status;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
public class BoardUpdateRequest {

    @NotBlank(message = "게시물 번호는 필수 항목입니다.")
    private Long id;

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    private String contents;

    //TODO : [Board]카테고리 수 제한 할건지 확인 필요
    @Nullable
    private List<String> categoryName;

    private Status status;

    @Builder
    public BoardUpdateRequest(Long id, String title, String contents, @Nullable List<String> categoryName, Status status) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.categoryName = categoryName;
        this.status = status;
    }
}
