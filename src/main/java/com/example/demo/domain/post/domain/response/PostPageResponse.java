package com.example.demo.domain.post.domain.response;


import com.example.demo.domain.post.domain.page.PageInfo;
import com.example.demo.domain.post.domain.page.PageTitleInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostPageResponse {
    private List<PageTitleInfo> pageTitleInfoList; // 페이지 제목 관련

    private PageInfo pageInfo; // 페이지 숫자 관련

    public PostPageResponse(List<PageTitleInfo> pageTitleInfoList, PageInfo pageInfo) {
        this.pageTitleInfoList = pageTitleInfoList;
        this.pageInfo = pageInfo;
    }
}
