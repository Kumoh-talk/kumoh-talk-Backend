package com.example.demo.domain.post_qna.controller;


import com.example.demo.domain.post.post_qna.domain.request.QnaRequest;
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


    @PostMapping("/save/{postId}")
    public ResponseEntity<QnaInfoResponse> qnaSave(@RequestBody @Valid QnaRequest qnaRequest,
                                                   @PathVariable Long postId) {
        return ResponseEntity.ok(qnaService.save(qnaRequest,postId));
    }

    @PatchMapping("/update/{qnaId}")
    public ResponseEntity<QnaInfoResponse> qnaUpdate(@RequestBody @Valid QnaRequest qnaRequest,
                                                     @PathVariable Long qnaId) {
        return ResponseEntity.ok(qnaService.update(qnaRequest,qnaId));
    }

    @PatchMapping("/delete/{qnaId}")
    public ResponseEntity<Void> qnaDelete(@PathVariable Long qnaId) {
        qnaService.delete(qnaId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/postqna/{postId}")
    public ResponseEntity<List<QnaInfoResponse>> qnaFindByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(qnaService.findByPostId(postId));
    }



}
