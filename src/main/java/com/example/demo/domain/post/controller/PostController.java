package com.example.demo.domain.post.controller;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.post.Repository.PostRepository;
import com.example.demo.domain.post.domain.request.PostCreateRequest;
import com.example.demo.domain.post.domain.response.PostCreateResponse;
import com.example.demo.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/save")
    public ResponseEntity<PostCreateResponse> postSave(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid PostCreateRequest postRequest) {
        return ResponseEntity.ok(postService.postSave(postRequest, user.getId()));
    }

//    @PatchMapping("/update")
//    public ResponseEntity<PostCreateResponse> postUpdate(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid PostCreateRequest postRequest) {
//        return ResponseEntity.ok(postService.postUpdate(postRequest, user.getId()));
//    }













}
