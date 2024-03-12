package com.example.demo.domain.post.controller;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.post.domain.request.PostRequest;
import com.example.demo.domain.post.domain.response.PostInfoResponse;
import com.example.demo.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/save")
    public ResponseEntity<PostInfoResponse> postSave(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid PostRequest postRequest) {
        CheckAuthentication(user);

        return ResponseEntity.ok(postService.postSave(postRequest, user.getId()));
    }


    @PatchMapping("/update/{postId}")
    public ResponseEntity<PostInfoResponse> postUpdate(@AuthenticationPrincipal UserPrincipal user,
                                                       @RequestBody @Valid PostRequest postRequest,
                                                       @PathVariable Long postId) {
        CheckAuthentication(user);
        return ResponseEntity.ok(postService.postUpdate(postRequest,user.getName(),postId));
    }


    @PatchMapping("/delete/{postId}")
    public ResponseEntity postUpdate(@AuthenticationPrincipal UserPrincipal user,@PathVariable Long postId) {
        CheckAuthentication(user);
        postService.postRemove(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<PostInfoResponse>> findAll(@AuthenticationPrincipal UserPrincipal user) {
        CheckAuthentication(user);

        return ResponseEntity.ok(postService.findByALL());
    }



    private void CheckAuthentication(UserPrincipal user) {
        if(user == null){
            new NullPointerException("인증된 객체에 값이 없습니다.");
        }
    }


}
