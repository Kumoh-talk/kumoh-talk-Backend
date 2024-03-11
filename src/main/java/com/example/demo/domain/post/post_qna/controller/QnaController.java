package com.example.demo.domain.post.post_qna.controller;


import com.example.demo.domain.post.post_qna.domain.request.QnaSaveRequest;
import com.example.demo.domain.post.post_qna.domain.request.QnaUpdateRequest;
import com.example.demo.domain.post.post_qna.domain.response.QnaInfoResponse;
import com.example.demo.domain.post.post_qna.service.QnaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("/update")
    public ResponseEntity<QnaInfoResponse> qnaUpdate(@RequestBody @Valid QnaUpdateRequest qnaUpdateRequest) {
        return ResponseEntity.ok(qnaService.update(qnaUpdateRequest));
    }

    @PatchMapping("/delete")
    public ResponseEntity<Void> qnaDelete(@RequestParam @NotBlank(message = "Qna 고유id를 작성해주세요") Long qnaId) {
        qnaService.delete(qnaId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/postqna")
    public ResponseEntity<List<QnaInfoResponse>> qnaFindByPostId(@RequestParam @NotBlank(message = " 게시글의 고유 id가 누락 되었습니다.")
                                                                     Long postId) {
        return ResponseEntity.ok(qnaService.findByPostId(postId));
    }



}
