package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class RecruitmentBoardPageNumResponse {
    private int pageSize;
    private int pageNum;
    private int totalPage;
    private String pageSort;
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
