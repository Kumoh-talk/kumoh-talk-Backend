package com.example.demo.domain.newsletter.controller;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterSubscribeRequest;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterUpdateEmailRequest;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterUpdateNotifyRequest;
import com.example.demo.domain.newsletter.domain.dto.response.NewsletterInfo;
import com.example.demo.domain.newsletter.event.EmailNotificationEvent;
import com.example.demo.domain.newsletter.service.NewsletterService;
import com.example.demo.domain.newsletter.strategy.*;
import com.example.demo.domain.recruitment_board.domain.dto.vo.BoardType;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.oauth.user.OAuth2Provider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;
import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/newsletters")
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping("/subscribe")
    public ResponseEntity<ResponseBody<Void>> subscribe(@RequestBody @Valid NewsletterSubscribeRequest request) {
        newsletterService.subscribe(request);
        return ResponseEntity.ok(createSuccessResponse());
    }

//    @AssignUserId
//    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
//    @GetMapping ("/subscribe")
//    public ResponseEntity<ResponseBody<NewsletterInfo>> getNewsletterInfo(Long userId) {
//        return ResponseEntity.ok(createSuccessResponse(newsletterService.getNewsletterInfo(userId)));
//    }

//    @PatchMapping("/subscribe/email")
//    public ResponseEntity<ResponseBody<Void>> updateNewsletterEmail(@RequestBody @Valid NewsletterUpdateEmailRequest request) {
//        newsletterService.updateNewsletterEmail(userId, request);
//        return ResponseEntity.ok(createSuccessResponse());
//    }

    @PatchMapping("/subscribe")
    public ResponseEntity<ResponseBody<Void>> updateNewsletterNotify(@RequestParam @Pattern(regexp = EMAIL_REGEXP, message = "이메일 정규식을 맞춰주세요.") String email,
                                                                     @RequestBody @Valid NewsletterUpdateNotifyRequest request) {
        newsletterService.updateNewsletterNotify(email, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @DeleteMapping("/subscribe")
    public ResponseEntity<ResponseBody<Void>> deleteNewsletterInfo(@RequestParam @Pattern(regexp = EMAIL_REGEXP, message = "이메일 정규식을 맞춰주세요.") String email) {
        newsletterService.deleteNewsletterInfo(email);
        return ResponseEntity.ok(createSuccessResponse());
    }

    // TODO. 이메일 테스트용 추후 삭제
//    private final ApplicationEventPublisher eventPublisher;
//    @Transactional
//    @GetMapping ("/test")
//    public ResponseEntity<ResponseBody<Void>> test() {
//        User user = User.builder()
//                .provider(OAuth2Provider.GOOGLE) // OAuth2Provider의 실제 값을 설정
//                .providerId("provider-id-example") // 실제 providerId로 변경
//                .nickname("테스트 닉네임 입니다") // 사용자 닉네임 설정
//                .role(Role.ROLE_ACTIVE_USER) // Role을 적절히 설정
//                .build();
//
//        Board board = new Board(
//                "세미나 제목",           // title
//                "세미나 내용",           // content
//                user,                   // user
//                com.example.demo.domain.board.domain.dto.vo.BoardType.SEMINAR,                    // tag
//                Status.DRAFT,                 // status
//                "https://example.com/image.jpg" // headImageUrl
//        );
//
//        RecruitmentBoard recruitmentBoard1 = RecruitmentBoard.builder()
//                .title("멘토링 제목입니다 테스트")
//                .summary("멘토링 요약 예시")
//                .content("멘토링 내용 예시")
//                .type(RecruitmentBoardType.MENTORING)
//                .tag(RecruitmentBoardTag.BACKEND) // 실제 태그로 변경
//                .status(Status.DRAFT) // 적절한 상태로 변경
//                .recruitmentTarget("멘토링 대상 예시")
//                .recruitmentNum("5") // 모집 인원 수
//                .recruitmentDeadline(LocalDateTime.now().plusDays(7)) // 마감일 예시
//                .activityStart(LocalDateTime.now().plusDays(1)) // 활동 시작일 예시
//                .activityFinish(LocalDateTime.now().plusDays(15)) // 활동 종료일 예시
//                .activityCycle("주간") // 활동 주기 예시
//                .user(user) // 실제 User 객체로 변경
//                .build();
//
//        RecruitmentBoard recruitmentBoard2 = RecruitmentBoard.builder()
//                .title("멘토링 제목입니다 테스트")
//                .summary("멘토링 요약 예시")
//                .content("멘토링 내용 예시")
//                .type(RecruitmentBoardType.PROJECT)
//                .tag(RecruitmentBoardTag.BACKEND) // 실제 태그로 변경
//                .status(Status.DRAFT) // 적절한 상태로 변경
//                .recruitmentTarget("멘토링 대상 예시")
//                .recruitmentNum("5") // 모집 인원 수
//                .recruitmentDeadline(LocalDateTime.now().plusDays(7)) // 마감일 예시
//                .activityStart(LocalDateTime.now().plusDays(1)) // 활동 시작일 예시
//                .activityFinish(LocalDateTime.now().plusDays(15)) // 활동 종료일 예시
//                .activityCycle("주간") // 활동 주기 예시
//                .user(user) // 실제 User 객체로 변경
//                .build();
//
//        RecruitmentBoard recruitmentBoard3 = RecruitmentBoard.builder()
//                .title("멘토링 제목입니다 테스트")
//                .summary("멘토링 요약 예시")
//                .content("멘토링 내용 예시")
//                .type(RecruitmentBoardType.STUDY)
//                .tag(RecruitmentBoardTag.BACKEND) // 실제 태그로 변경
//                .status(Status.DRAFT) // 적절한 상태로 변경
//                .recruitmentTarget("멘토링 대상 예시")
//                .recruitmentNum("5") // 모집 인원 수
//                .recruitmentDeadline(LocalDateTime.now().plusDays(7)) // 마감일 예시
//                .activityStart(LocalDateTime.now().plusDays(1)) // 활동 시작일 예시
//                .activityFinish(LocalDateTime.now().plusDays(15)) // 활동 종료일 예시
//                .activityCycle("주간") // 활동 주기 예시
//                .user(user) // 실제 User 객체로 변경
//                .build();
//
//        eventPublisher.publishEvent(
//                EmailNotificationEvent.create(
//                        BoardType.MENTORING,
//                        MentoringNoticeEmailDeliveryStrategy.create(recruitmentBoard1)));
//        eventPublisher.publishEvent(
//                EmailNotificationEvent.create(
//                        BoardType.PROJECT,
//                        ProjectNoticeEmailDeliveryStrategy.create(recruitmentBoard2)));
//        eventPublisher.publishEvent(
//                EmailNotificationEvent.create(
//                        BoardType.SEMINAR_SUMMARY,
//                        SeminarSummaryEmailDeliveryStrategy.create(board)));
//        eventPublisher.publishEvent(
//                EmailNotificationEvent.create(
//                        BoardType.STUDY,
//                        StudyNoticeEmailDeliveryStrategy.create(recruitmentBoard3)));
//        return ResponseEntity.ok(createSuccessResponse());
//    }
}
