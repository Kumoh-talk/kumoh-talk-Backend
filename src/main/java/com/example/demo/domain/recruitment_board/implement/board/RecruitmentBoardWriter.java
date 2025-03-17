package com.example.demo.domain.recruitment_board.implement.board;

import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardAndFormInfo;
import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecruitmentBoardWriter {
    private final RecruitmentBoardRepository recruitmentBoardRepository;

    public RecruitmentBoardAndFormInfo post(RecruitmentBoardAndFormInfo recruitmentBoardAndFormInfo) {
        return recruitmentBoardRepository.save(recruitmentBoardAndFormInfo);
    }

    public RecruitmentBoardAndFormInfo patch(RecruitmentBoardAndFormInfo recruitmentBoardAndFormInfo) {
        return recruitmentBoardRepository.patch(recruitmentBoardAndFormInfo);
    }

    public void delete(RecruitmentBoardInfo recruitmentBoardInfo) {
        recruitmentBoardRepository.delete(recruitmentBoardInfo);
    }

    public void increaseCurrentMemberNum(Long recruitmentBoardId) {
        recruitmentBoardRepository.increaseCurrentMemberNum(recruitmentBoardId);
    }

    public void decreaseCurrentMemberNum(Long recruitmentBoardId) {
        recruitmentBoardRepository.decreaseCurrentMemberNum(recruitmentBoardId);
    }
}
