package com.example.demo.domain.application.controller;

import com.example.demo.domain.application.service.ApplicationService;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/application")
@RequiredArgsConstructor
public class ApplicationController {
    private ApplicationService applicationService;

//    @PostMapping()
//    public ResponseEntity<ResponseBody<Void>> createApplication(){
//
//    }
//    @GetMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> getApplicationList(){
//
//    }
//    @GetMapping("/{applicationBoardId}/{applicantId}")
//    public ResponseEntity<ResponseBody<Void>> getApplicationInfoByApplicantId(){
//
//    }
//    @GetMapping("/{applicationBoardId}/{userId}")
//    public ResponseEntity<ResponseBody<Void>> getApplicationInfoByUserId(){
//
//    }
//    @PatchMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> updateApplication(){
//
//    }
//    @DeleteMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> deleteApplication(){
//
//    }
}
