package com.example.demo.domain.board.service;

import com.example.demo.domain.board.domain.Board;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.entity.UploadFile;
import com.example.demo.domain.file.uploader.FileUploader;
import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.page.PageInfo;
import com.example.demo.domain.board.domain.page.PageSort;
import com.example.demo.domain.board.domain.page.PageTitleInfo;
import com.example.demo.domain.board.domain.request.BoardRequest;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.response.BoardPageResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileUploader fileUploader;

    private static final int PAGE_SIZE = 10;
    /**
     *
     * @param boardRequest
     * @param userId
     * @return PostInfoResponse 생성된 post 정보 객체 반환
     */
    @Transactional
    public BoardInfoResponse postSave(BoardRequest boardRequest, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다"));

        Board board = BoardRequest.toEntity(boardRequest, user);
        Board savedBoard = boardRepository.save(board);

        FileNameInfo attachfileNameInfo = fileUploader.storeFile(boardRequest.getAttachFile(), savedBoard);
        List<FileNameInfo> imagesFileNameInfos = fileUploader.storeFiles(boardRequest.getImageFiles(), savedBoard);

        return BoardInfoResponse.from(
                savedBoard,
                user.getName(),
                attachfileNameInfo,
                imagesFileNameInfos);
    }


    /**
     *
     * @param boardRequest
     * @param userName
     * @param postId
     * @return
     */
    @Transactional
    public BoardInfoResponse postUpdate(BoardRequest boardRequest, String userName, Long postId) throws IOException {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new ServiceException(ErrorCode.POST_NOT_FOUND));
        if(!board.getUser().getName().equals(userName)) {
            new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
        return updatePost(boardRequest, board, userName);
    }


    /**
     * 게시물 삭제 메서드
     * @param postId
     */
    @Transactional
    public void postRemove(Long postId,String userName) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new ServiceException(ErrorCode.POST_NOT_FOUND));
        if(!board.getUser().getName().equals(userName)) {
            new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
        fileUploader.deletePostFiles(board);
        boardRepository.delete(board);
    }
    /**
     * 게시물 id로 게시물을 찾는 메서드
     * @param postId
     * @param userName
     * @return
     */
    @Transactional(readOnly = true)
    public BoardInfoResponse findById(Long postId, String userName) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new ServiceException(ErrorCode.POST_NOT_FOUND));
        List<UploadFile> uploadFiles = board.getUploadFiles();
        return BoardInfoResponse.from(board,
                userName,
                fileUploader.getPostAttachFile(uploadFiles),
                fileUploader.getPostImagesFiles(uploadFiles));
    }

    /**
     * 전체 게시물을 불러오는 메서드
     * @return List<PostInfoResponse>
     */
    @Transactional(readOnly = true)
    public List<BoardInfoResponse> findByALL() {
/*        return postRepository.findAll().stream()
                .map(post -> PostInfoResponse.from(post, post.getUser().getName()))
                .collect(Collectors.toList());*/
        return null;  // 추후 pagging 처리 추가
    }

    @Transactional(readOnly = true)
    public BoardPageResponse findPageList(int page, Track track, PageSort pageSort) {
        PageRequest pageRequest = (pageSort == PageSort.DESC) ? PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending()):
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending());


        Page<Board> postPage = boardRepository.findAllByTrack(track, pageRequest);

        PageInfo pageInfo = new PageInfo(postPage.getSize(), postPage.getNumber(), postPage.getTotalPages(), pageSort);

        List<PageTitleInfo> pageTitleInfoList = new ArrayList<>();
        postPage.forEach(post -> {
            pageTitleInfoList.add(PageTitleInfo.from(post, post.getUser().getName()));
        });


        return new BoardPageResponse(pageTitleInfoList, pageInfo);
    }

    public BoardInfoResponse updatePost(BoardRequest boardRequest, Board board, String userName) throws IOException {

        fileUploader.deletePostFiles(board);
        FileNameInfo attachfileNameInfo = fileUploader.storeFile(boardRequest.getAttachFile(), board);
        List<FileNameInfo> imagesFileNameInfos = fileUploader.storeFiles(boardRequest.getImageFiles(), board);
        board.setTitle(boardRequest.getTitle());
        board.setContents(boardRequest.getContents());

        return BoardInfoResponse.from(board, userName, attachfileNameInfo, imagesFileNameInfos);
    }



}
