package com.example.demo.domain.board.service.usecase;

import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.service.service.LikeService;
import com.example.demo.domain.board.service.service.BoardCommandService;
import com.example.demo.domain.board.service.service.BoardQueryService;
import com.example.demo.domain.board.service.service.ViewIncreaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardUseCase {
    private final BoardCommandService boardCommandService;
    private final BoardQueryService boardQueryService;
    private final ViewIncreaseService viewIncreaseService;
    private final LikeService likeService;

    /**
     * 게시물 임시 저장 무조건 초기 게시물 상태는 임시 저장 상태임
     * @param userId
     * @param boardCreateRequest
     * @return BoardInfoResponse
     */
    @Transactional
    public BoardInfoResponse saveDraftBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        return boardCommandService.createBoard(userId, boardCreateRequest);
    }

    /**
     * 게시물 조회 ( 조회수 증가 기능은 무조건 1 증가 하는 것으로 해놨습니다. 이후 수정이 필요할 수 있습니다. )
     * @param boardId
     * @return BoardInfoResponse
     */
    @Transactional
    public BoardInfoResponse searchSingleBoard(Long boardId) {
        viewIncreaseService.increaseView(boardId);
        return boardQueryService.findByboardId(boardId);
    }

    /**
     * 게시물 수정
     * @param userId
     * @param boardUpdateRequest
     * @return BoardInfoResponse
     */
    @Transactional
    public BoardInfoResponse updateBoard(Long userId, BoardUpdateRequest boardUpdateRequest) {
        return boardCommandService.updateBoard(boardUpdateRequest, userId);
    }

    /**
     * 게시물 삭제
     * @param userId
     * @param boardId
     */
    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        boardCommandService.removeBoard(userId, boardId);
    }

    /**
     * 게시물 좋아요
     * @param userId
     * @param boardId
     */
    @Transactional
    public void likeBoard(Long userId, Long boardId) {
        likeService.increaseLike(userId, boardId);
    }

}
