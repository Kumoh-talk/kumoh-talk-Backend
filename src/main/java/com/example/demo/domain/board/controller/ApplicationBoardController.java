package com.example.demo.domain.board.controller;

import com.example.demo.domain.board.service.ApplicationBoardService;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/board/application")
@RequiredArgsConstructor
public class ApplicationBoardController {
    private final ApplicationBoardService applicationService;
//    @PostMapping()
//    public ResponseEntity<ResponseBody<Void>> createApplicationPostAndForm(){
//
//    }
//
//    @GetMapping(value = {"/study", "/project"})
//    public ResponseEntity<ResponseBody<Void>> getApplicationPostsList(){
//
//    }
//
//    @GetMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> getApplicationPostInfo(@PathVariable String applicationBoardId){
//
//    }
//
//    @GetMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> getApplicationFormInfo(@PathVariable String applicationBoardId){
//
//    }
//
//    @PatchMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> updateApplicationPostAndForm(@PathVariable String applicationBoardId){
//
//    }
//
//    @DeleteMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> deleteApplicationPostAndForm(@PathVariable String applicationBoardId){
//
//    }
}
