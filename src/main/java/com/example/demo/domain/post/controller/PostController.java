package com.example.demo.domain.post.controller;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.post.domain.request.PostCreateRequest;
import com.example.demo.domain.post.domain.request.PostUpdateRequest;
import com.example.demo.domain.post.domain.response.PostInfoResponse;
import com.example.demo.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/save")
    public ResponseEntity<PostInfoResponse> postSave(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid PostCreateRequest postRequest) {
        CheckAuthentication(user);

        return ResponseEntity.ok(postService.postSave(postRequest, user.getId()));
    }


    @PatchMapping("/update")
    public ResponseEntity<PostInfoResponse> postUpdate(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid PostUpdateRequest postUpdateRequest) {
        CheckAuthentication(user);
        return ResponseEntity.ok(postService.postUpdate(postUpdateRequest,user.getName()));
    }


    @PatchMapping("/delete")
    public ResponseEntity postUpdate(@AuthenticationPrincipal UserPrincipal user,@RequestBody Long postId) {
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


/*    @BeforeEach
    public void setUp(){
        SecurityContext context = SecurityContextHolder.clearContext();
        MockHttpREqu
        context.setAuthentication();
    }

    @After

    @Test
    public void test(){
        mvcbuilder.post()
                .apply()
    }*/

//    @PatchMapping("/update")
//    public ResponseEntity<PostCreateResponse> postUpdate(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid PostCreateRequest postRequest) {
//        return ResponseEntity.ok(postService.postUpdate(postRequest, user.getId()));
//    }













}
