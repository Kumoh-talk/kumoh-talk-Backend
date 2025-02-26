package com.example.demo.application.newsletter.controller;

import com.example.demo.application.newsletter.api.NewsletterAdminApi;
import com.example.demo.application.newsletter.dto.request.EmailNoticeRequest;
import com.example.demo.application.newsletter.dto.response.SeminarNoticeBasicForm;
import com.example.demo.domain.newsletter.service.NewsletterAdminService;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/newsletters")
@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
public class NewsletterAdminController implements NewsletterAdminApi {

    private final NewsletterAdminService newsletterAdminService;

    /**
     * html 폼에 따른 세미나 공지사항 전송 api
     * @param request subject, htmlContent
     */
    @PostMapping
    public ResponseEntity<ResponseBody<Void>> sendNoticeByEmail(@Valid @RequestBody EmailNoticeRequest request) {
        request.validateHtmlContent();
        newsletterAdminService.sendNoticeByEmail(request.subject(), request.htmlContent());
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * 세미나 공지사항 기본 폼 제공 api
     */
    @GetMapping("/seminar-notice-basicFrom")
    public ResponseEntity<ResponseBody<SeminarNoticeBasicForm>> getSeminarNoticeBasicForm() {
        return ResponseEntity.ok(createSuccessResponse(new SeminarNoticeBasicForm(newsletterAdminService.getSeminarNoticeBasicForm().getHtmlContent())));
    }
}
