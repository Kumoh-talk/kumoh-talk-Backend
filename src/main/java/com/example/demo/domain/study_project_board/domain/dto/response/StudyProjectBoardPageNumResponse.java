package com.example.demo.domain.study_project_board.domain.dto.response;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class StudyProjectBoardPageNumResponse {
    private int pageSize;
    private int pageNum;
    private int totalPage;
    private String pageSort;
    private List<StudyProjectBoardNoOffsetResponse.StudyProjectBoardSummaryInfo> boardInfo;

    public static StudyProjectBoardPageNumResponse from(Page<StudyProjectBoard> studyProjectBoardList) {

        return StudyProjectBoardPageNumResponse.builder()
                .pageSize(studyProjectBoardList.getSize())
                .pageNum(studyProjectBoardList.getNumber() + 1)
                .totalPage(studyProjectBoardList.getTotalPages())
                .pageSort(studyProjectBoardList.getSort().toString())
                .boardInfo(studyProjectBoardList.getContent().stream()
                        .map(StudyProjectBoardNoOffsetResponse.StudyProjectBoardSummaryInfo::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
