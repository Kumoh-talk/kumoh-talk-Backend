package com.example.demo.domain.comment.implement.board;

import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentRecruitmentBoardReaderImpl implements GenericCommentBoardReader {
    private final RecruitmentBoardRepository recruitmentBoardRepository;

    @Override
    public Long getById(Long id) {
        RecruitmentBoardInfo recruitmentBoard = recruitmentBoardRepository.getById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        return recruitmentBoard.getBoardId();
    }

    @Override
    public Long getByIdWithUser(Long id) {
        RecruitmentBoardInfo recruitmentBoard = recruitmentBoardRepository.getByIdWithUser(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        return recruitmentBoard.getBoardId();
    }
}
