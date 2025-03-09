package com.example.demo.domain.recruitment_board.implement.form;

import com.example.demo.domain.recruitment_board.entity.RecruitmentFormQuestionInfo;
import com.example.demo.domain.recruitment_board.repository.RecruitmentFormQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecruitmentFormQuestionReader {
    private final RecruitmentFormQuestionRepository recruitmentFormQuestionRepository;

    public List<RecruitmentFormQuestionInfo> getByBoarIdWithAnswerList(Long recruitmentBoardId) {
        return recruitmentFormQuestionRepository.getByBoardIdWithAnswerList(recruitmentBoardId);
    }

    public List<Long> getEssentialListByRecruitmentBoardId(Long boardId) {
        return recruitmentFormQuestionRepository.getEssentialListByRecruitmentBoardId(boardId);
    }
}
