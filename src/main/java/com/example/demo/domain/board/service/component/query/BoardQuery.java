package com.example.demo.domain.board.service.component.query;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.Repository.ViewRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.View;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQuery {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public BoardInfoResponse findByboardId(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        validateBoardStatus(board); //TODO : [Board]조회수 증가 로직 신고 글 안보임 수정 사항이 많음
        validateReportedBoard(board);  //TODO : [Board]report 기능 추가 시 로직 변경

        return BoardInfoResponse.from(
                board,
                board.getUser().getNickname(),
                boardRepository.countViewsByBoardId(boardId),
                boardRepository.countLikesByBoardId(boardId),
                boardRepository.findCategoryNameByBoardId(boardId));
    }

    private void validateReportedBoard(Board board) { //TODO : [Board]report 기능 추가 시 로직 추가
    }

    private void validateBoardStatus(Board board) {
        if(board.getStatus().equals("DRAFT")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = (Long) authentication.getPrincipal();
            if(!board.getUser().getId().equals(userId)) {
                throw new ServiceException(ErrorCode.BOARD_NOT_FOUND);
            }
        }
    }

//    @Transactional(readOnly = true)
//    public BoardPageResponse findPageList(int page, Track track, PageSort pageSort) {
//        PageRequest pageRequest = (pageSort == PageSort.DESC) ? PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending()):
//                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending());
//
//
//        Page<Board> postPage = boardRepository.findAllByTrack(track, pageRequest);
//
//        PageInfo pageInfo = new PageInfo(postPage.getSize(), postPage.getNumber(), postPage.getTotalPages(), pageSort);
//
//        List<PageTitleInfo> pageTitleInfoList = new ArrayList<>();
//        postPage.forEach(post -> {
//            pageTitleInfoList.add(PageTitleInfo.from(post, post.getUser().getName()));
//        });
//
//
//        return new BoardPageResponse(pageTitleInfoList, pageInfo);
//    }

}
