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
    public boolean existsById(Long id) {
        return recruitmentBoardRepository.getById(id).isPresent();
    }

    @Override
    public boolean existsByIdWithUser(Long id) {
        return recruitmentBoardRepository.getByIdWithUser(id).isPresent();
    }

    @Override
    public Long getUserIdById(Long id) {
        RecruitmentBoardInfo boardInfo = recruitmentBoardRepository.getByIdWithUser(id).orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        return boardInfo.getUserId();
    }
}
