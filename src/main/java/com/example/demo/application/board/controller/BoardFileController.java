package com.example.demo.application.board.controller;

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

import com.example.demo.application.board.api.BoardFileApi;
import com.example.demo.application.board.dto.request.FileRequest;
import com.example.demo.application.board.dto.request.PresignedUrlRequest;
import com.example.demo.domain.board.service.usecase.BoardFileService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardFileController implements BoardFileApi {
	private final BoardFileService boardFileService;

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
	@PostMapping("/v1/boards/files/presigned-url")
	public ResponseEntity<ResponseBody<String>> getPresignedUrl(Long userId,
		@RequestBody @Valid PresignedUrlRequest presignedUrlRequest) {
		return ResponseEntity.ok(createSuccessResponse(
			boardFileService.getPresignedUrl(userId, presignedUrlRequest.boardId(),presignedUrlRequest.toBoardFileInfo())));
	}

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
	@PostMapping("/v1/boards/files/images")
	public ResponseEntity<ResponseBody<Void>> saveImageFileUrl(Long userId,@RequestBody @Valid FileRequest fileRequest) {
		boardFileService.saveImageFileUrl(userId,fileRequest.boardId() ,fileRequest.toBoardFileInfo());
		return ResponseEntity.ok(createSuccessResponse());
	}

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
	@PatchMapping("/v1/boards/files/attach")
	public ResponseEntity<ResponseBody<Void>> changeAttachFileUrl(Long userId,@RequestBody @Valid FileRequest fileRequest) {
		boardFileService.changeAttachFileUrl(userId, fileRequest.boardId() ,fileRequest.toBoardFileInfo());
		return ResponseEntity.ok(createSuccessResponse());
	}

	@GetMapping("/v1/boards/files/attach/{boardId}")
	public ResponseEntity<ResponseBody<String>> getAttachFileUrl(@PathVariable Long boardId) {
		return ResponseEntity.ok(createSuccessResponse(boardFileService.getAttachFileUrl(boardId)));
	}


	/*
	 * @Deprecated됨
	 * 리팩터링 과정에서 의미없는 메소드로 판단되어 Deprecated 처리함
	 */
	@Deprecated
	@AssignUserId
	@PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
	@DeleteMapping("/v1/boards/files/images")
	public ResponseEntity<ResponseBody<Void>> deleteImageFileUrl(Long userId,@RequestBody @Valid FileRequest fileRequest) {
		// boardFileService.deleteImageFileUrl(userId,  fileRequest.boardId() ,fileRequest.toBoardFileInfo());
		return ResponseEntity.ok(createSuccessResponse());
	}


}
