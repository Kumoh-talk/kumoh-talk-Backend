package com.example.demo.domain.board.controller;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.board.domain.dto.request.FileRequest;
import com.example.demo.domain.board.domain.dto.request.PresignedUrlRequest;
import com.example.demo.domain.board.service.usecase.BoardFileUseCase;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardFileController {
	private final BoardFileUseCase boardFileUseCase;

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
	@PostMapping("/v1/boards/files/presigned-url")
	public ResponseEntity<ResponseBody<String>> getPresignedUrl(Long userId,
		@RequestBody @Valid PresignedUrlRequest presignedUrlRequest) {
		return ResponseEntity.ok(createSuccessResponse(boardFileUseCase.getPresignedUrl(userId, presignedUrlRequest)));
	}

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
	@PostMapping("/v1/boards/files/images")
	public ResponseEntity<ResponseBody<Void>> saveImageFileUrl(Long userId,@RequestBody @Valid FileRequest fileRequest) {
		boardFileUseCase.saveImageFileUrl(userId, fileRequest);
		return ResponseEntity.ok(createSuccessResponse());
	}

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
	@PatchMapping("/v1/boards/files/attach")
	public ResponseEntity<ResponseBody<Void>> changeAttachFileUrl(Long userId,@RequestBody @Valid FileRequest fileRequest) {
		boardFileUseCase.changeAttachFileUrl(userId, fileRequest);
		return ResponseEntity.ok(createSuccessResponse());
	}

	@GetMapping("/v1/boards/files/attach/{boardId}")
	public ResponseEntity<ResponseBody<String>> getAttachFileUrl(@PathVariable Long boardId) {
		return ResponseEntity.ok(createSuccessResponse(boardFileUseCase.getAttachFileUrl(boardId)));
	}

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
	@DeleteMapping("/v1/boards/files/images")
	public ResponseEntity<ResponseBody<Void>> deleteImageFileUrl(Long userId,@RequestBody @Valid FileRequest fileRequest) {
		boardFileUseCase.deleteImageFileUrl(userId, fileRequest);
		return ResponseEntity.ok(createSuccessResponse());
	}


}
