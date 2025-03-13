package com.example.demo.application.comment.api;

import com.example.demo.application.comment.dto.request.CommentRequest;
import com.example.demo.application.comment.dto.response.CommentInfoResponse;
import com.example.demo.application.comment.dto.response.CommentResponse;
import com.example.demo.application.comment.dto.response.MyCommentResponse;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CommentApi<T> {
    ResponseEntity<ResponseBody<CommentResponse>> getBoardComments(
            @PathVariable Long boardId);

    ResponseEntity<ResponseBody<GlobalPageResponse<MyCommentResponse>>> getUserComments(
            Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam T boardType);

    ResponseEntity<ResponseBody<CommentInfoResponse>> postComment(
            Long userId,
            @PathVariable Long boardId,
            @RequestBody @Valid CommentRequest commentRequest);

    ResponseEntity<ResponseBody<CommentInfoResponse>> patchComment(
            Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest commentRequest);

    ResponseEntity<ResponseBody<Void>> deleteComment(
            Long userId,
            @PathVariable Long commentId);


}
