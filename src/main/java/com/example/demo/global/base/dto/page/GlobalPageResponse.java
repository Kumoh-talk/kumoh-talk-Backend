package com.example.demo.global.base.dto.page;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class GlobalPageResponse {
	private final int pageSize; // 한 페이지에 게시물 갯수
	private final int pageNum; // 현재 페이지 number
	private final int totalPage; // 총 페이지 갯수
	private final String pageSort; // 정렬 ( 내림 or 오름)
	private final List<?> pageContents; // 페이지 제목 관련

	private GlobalPageResponse(int pageSize, int pageNum, int totalPage, String pageSort,
		List<?> pageContents) {
		this.pageSize = pageSize;
		this.pageNum = pageNum;
		this.totalPage = totalPage;
		this.pageSort = pageSort;
		this.pageContents = pageContents;
	}


	public static GlobalPageResponse from(Page<?> pageDTO) {
		return new GlobalPageResponse(
			pageDTO.getNumber()+1,
			pageDTO.getSize(),
			pageDTO.getTotalPages(),
			pageDTO.getSort().toString(),
			pageDTO.getContent());
	}

}
