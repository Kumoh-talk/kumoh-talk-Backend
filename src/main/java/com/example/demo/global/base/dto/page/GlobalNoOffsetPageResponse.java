package com.example.demo.global.base.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(name = "GlobalNoOffsetPageResponse", description = "no-offset 방식 페이지 응답")
public class GlobalNoOffsetPageResponse<T> {
    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean nextPage;
    @Schema(description = "한 페이지에 게시물 갯수", example = "10")
    private int pageSize;
    @Schema(description = "페이징 내용")
    private List<T> pageContents;

    @Builder
    public GlobalNoOffsetPageResponse(boolean nextPage, int pageSize, List<T> pageContents) {
        this.nextPage = nextPage;
        this.pageSize = pageSize;
        this.pageContents = pageContents;
    }

    public static <T> GlobalNoOffsetPageResponse<T> from(
            int pageSize, List<T> pageList) {
        // pageList를 데이터베이스에서 가져올 때, PageSize+1 만큼 가져온 후 nextPage 여부 확인
        boolean nextPage = false;
        if (pageList.size() > pageSize) {
            nextPage = true;
            pageList.remove(pageSize);
        }
        return GlobalNoOffsetPageResponse.<T>builder()
                .nextPage(nextPage)
                .pageSize(pageSize)
                .pageContents(pageList)
                .build();
    }
}
