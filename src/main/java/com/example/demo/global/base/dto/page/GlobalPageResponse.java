package com.example.demo.global.base.dto.page;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "GlobalPageResponse", description = "페이지 응답")
public class GlobalPageResponse<T> {
	@Schema(name = "pageSize", description = "한 페이지에 게시물 갯수", example = "10")
	private final int pageSize; // 한 페이지에 게시물 갯수
	@Schema(name = "pageNum", description = "현재 페이지 number", example = "1")
	private final int pageNum; // 현재 페이지 number
	@Schema(name = "totalPage", description = "총 페이지 갯수", example = "10")
	private final int totalPage; // 총 페이지 갯수
	@Schema(name = "pageSort", description = "정렬 ( 내림 or 오름)", example = "DESC")
	private final String pageSort; // 정렬 ( 내림 or 오름)
	@Schema(name = "pageContents", description = "페이징 내용")
	private final List<T> pageContents; // 페이지 제목 관련

	private GlobalPageResponse(int pageSize, int pageNum, int totalPage, String pageSort,
		List<T> pageContents) {
		this.pageSize = pageSize;
		this.pageNum = pageNum;
		this.totalPage = totalPage;
		this.pageSort = pageSort;
		this.pageContents = pageContents;
	}

	public static <T> GlobalPageResponse<T> create(
			Page<T> pageDTO) {
		return new GlobalPageResponse<>(
                pageDTO.getNumber() + 1,
                pageDTO.getSize(),
                pageDTO.getTotalPages(),
                pageDTO.getSort().toString(),
                pageDTO.getContent());
	}
}
