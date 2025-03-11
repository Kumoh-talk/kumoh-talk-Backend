package com.example.demo.domain.comment.implement.board;

import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentRecruitmentBoardReaderImpl implements GenericCommentBoardReader {
    private final RecruitmentBoardRepository recruitmentBoardRepository;

    // TODO : RecruitmentBoard 리팩토링 후 다시 손 봐야함
    @Override
    public boolean existsById(Long id) {
        return recruitmentBoardRepository.doFindById(id).isPresent();
    }

    @Override
    public boolean existsByIdWithUser(Long id) {
        return recruitmentBoardRepository.findByIdWithUser(id).isPresent();
    }
}
