package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentBoardPageNumResponse", description = "페이지 번호 방식 모집 게시물 페이지 응답")
public class RecruitmentBoardPageNumResponse {
    @Schema(description = "한 페이지에 게시물 갯수", example = "10")
    private int pageSize;
    @Schema(description = "현재 페이지 number", example = "1")
    private int pageNum;
    @Schema(description = "총 페이지 갯수", example = "10")
    private int totalPage;
    @Schema(description = "정렬 기준 및 방향", example = "createdAt: DESC")
    private String pageSort;
    @Schema(description = "모집 게시물 요약 정보 리스트")
    private List<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> boardInfo;

    public static RecruitmentBoardPageNumResponse from(Page<RecruitmentBoard> recruitmentBoardList) {

        return RecruitmentBoardPageNumResponse.builder()
                .pageSize(recruitmentBoardList.getSize())
                .pageNum(recruitmentBoardList.getNumber() + 1)
                .totalPage(recruitmentBoardList.getTotalPages())
                .pageSort(recruitmentBoardList.getSort().toString())
                .boardInfo(recruitmentBoardList.getContent().stream()
                        .map(RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
