package com.example.demo.infra.recruitment_board.repository.impl;

import com.example.demo.domain.recruitment_board.entity.RecruitmentFormQuestionInfo;
import com.example.demo.domain.recruitment_board.repository.RecruitmentFormQuestionRepository;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentFormQuestionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecruitmentFormQuestionRepositoryImpl implements RecruitmentFormQuestionRepository {
    private final RecruitmentFormQuestionJpaRepository recruitmentFormQuestionJpaRepository;

    @Override
    public List<RecruitmentFormQuestionInfo> getByBoardIdWithAnswerList(Long recruitmentBoardId) {
        return recruitmentFormQuestionJpaRepository.findByBoardIdWithAnswerList(recruitmentBoardId).stream()
                .map(RecruitmentFormQuestion::toDomain).collect(Collectors.toList());
    }
}
