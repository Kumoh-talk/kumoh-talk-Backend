package com.example.demo.domain.board.domain.request;

import com.example.demo.domain.board.domain.vo.Status;
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

    //TODO : 카테고리 수 제한 할건지 확인 필요
    @Nullable
    private List<String> categoryName;

    private Status status;
}
