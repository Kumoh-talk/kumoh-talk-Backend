package com.example.demo.domain.comment.implement.board;

import com.example.demo.domain.recruitment_board.domain.entity.CommentBoard;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentRecruitmentBoardReaderImpl implements GenericCommentBoardReader {
    private final RecruitmentBoardRepository recruitmentBoardRepository;

    // TODO : RecruitmentBoard 리팩토링 후 다시 손 봐야함
    @Override
    public Long getById(Long id) {
        CommentBoard recruitmentBoard = recruitmentBoardRepository.doFindById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        return recruitmentBoard.getId();
    }

    @Override
    public Long getByIdWithUser(Long id) {
        CommentBoard recruitmentBoard = recruitmentBoardRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        return recruitmentBoard.getId();
    }
}
