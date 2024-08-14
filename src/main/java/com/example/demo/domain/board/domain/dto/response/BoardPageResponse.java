package com.example.demo.domain.board.domain.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

import org.springframework.data.domain.Page;

@Getter
public class BoardPageResponse {
    private final int pageSize; // 한 페이지에 게시물 갯수
    private final int pageNum; // 현재 페이지 number
    private final int totalPage; // 총 페이지 갯수
    private final String pageSort; // 정렬 ( 내림 or 오름)
    private final List<BoardTitleInfoResponse> boardTitleInfoResponses; // 페이지 제목 관련

    public BoardPageResponse(int pageNum, int pageSize, int totalPage,String pageSort ,List<BoardTitleInfoResponse> boardTitleInfoResponseList) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.pageSort = pageSort;
        this.boardTitleInfoResponses = boardTitleInfoResponseList;
    }

    public static BoardPageResponse from(Page<BoardTitleInfoResponse> boardTitleInfoResponsePage) {
        return new BoardPageResponse(
                boardTitleInfoResponsePage.getNumber()+1,
                boardTitleInfoResponsePage.getSize(),
                boardTitleInfoResponsePage.getTotalPages(),
                boardTitleInfoResponsePage.getSort().toString(),
                boardTitleInfoResponsePage.getContent());
    }
}
