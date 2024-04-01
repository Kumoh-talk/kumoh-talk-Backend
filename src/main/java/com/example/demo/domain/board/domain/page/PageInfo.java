package com.example.demo.domain.board.domain.page;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {
    private int pageSize; // 한 페이지에 게시물 갯수
    private int page; // 현재 페이지 number
    private int totalPage; // 총 페이지 갯수
    private PageSort pageSort; // 정렬 ( 내림 or 오름)


    public PageInfo(int pageSize, int page, int totalPage, PageSort pageSort) {
        this.pageSize = pageSize;
        this.page = page;
        this.totalPage = totalPage;
        this.pageSort = pageSort;
    }
}
