package com.example.demo.domain.post.post_qna.controller;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.post.domain.request.PostCreateRequest;
import com.example.demo.domain.post.domain.request.PostUpdateRequest;
import com.example.demo.domain.post.domain.response.PostInfoResponse;
import com.example.demo.domain.post.post_qna.domain.request.QnaSaveRequest;
import com.example.demo.domain.post.post_qna.domain.response.QnaInfoResponse;
import com.example.demo.domain.post.post_qna.service.QnaService;
import com.example.demo.domain.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qna")
public class QnaController {

    private QnaService qnaService;


    @PostMapping("/save")
    public ResponseEntity<QnaInfoResponse> qnaSave(@RequestBody @Valid QnaSaveRequest qnaSaveRequest) {

        return ResponseEntity.ok(qnaService.save(qnaSaveRequest));
    }







}
