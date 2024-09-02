package com.example.demo.domain.study_project_application.controller;

import com.example.demo.domain.study_project_application.service.StudyProjectApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applications/study-project")
@RequiredArgsConstructor
public class StudyProjectApplicationController {
    private StudyProjectApplicationService studyProjectApplicationService;

//    // 신청 추가
//    @AssignUserId
//    @PostMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> createApplication(
//            Long userId, ApplicationRequest applicationRequest){
//
//    }
//
//    // 게시물 별 신청 리스트 조회(applicant 테이블 get) -> 관리자 기능
//    @GetMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> getApplicationList(){
//
//    }
//
//    // applicant id를 사용하여 신청 정보 상세 조회 -> 관리자 기능
//    @GetMapping("/{applicationBoardId}/{applicantId}")
//    public ResponseEntity<ResponseBody<Void>> getApplicationInfoByApplicantId(){
//
//    }
//
//    // 해당 신청 게시물에 신청한 내용 수정 -> 마감기한 지나면 불가능
//    @PatchMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> updateApplication(){
//
//    }
//
//    // 해당 신청 게시물에 신청한 내용 삭제 -> 마감기한 지나면 불가능
//    @DeleteMapping("/{applicationBoardId}")
//    public ResponseEntity<ResponseBody<Void>> deleteApplication(){
//
//    }

    // user id를 사용하여 신청 리스트 조회(마이페이지)
}
