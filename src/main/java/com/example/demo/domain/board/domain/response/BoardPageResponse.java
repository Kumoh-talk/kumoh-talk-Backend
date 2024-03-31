package com.example.demo.domain.board.domain.response;


import com.example.demo.domain.board.domain.page.PageInfo;
import com.example.demo.domain.board.domain.page.PageTitleInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardPageResponse {
    private List<PageTitleInfo> pageTitleInfoList; // 페이지 제목 관련

    private PageInfo pageInfo; // 페이지 숫자 관련

    public BoardPageResponse(List<PageTitleInfo> pageTitleInfoList, PageInfo pageInfo) {
        this.pageTitleInfoList = pageTitleInfoList;
        this.pageInfo = pageInfo;
    }
}
