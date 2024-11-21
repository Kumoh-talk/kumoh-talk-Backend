package com.example.demo.domain.board.api;




import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.dto.response.DraftBoardTitleResponse;
import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

public interface BoardApi {
	@Operation(
		summary = "세미나/공지사항 게시물 임시 저장",
		description = "세미나/공지사항 게시물 임시저장을 합니다."
	)
	@ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoardInfoResponse.class)))
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = BoardInfoResponse.class,
			description = "게시물 DRAFT 상태로 임시저장 됩니다\n"
				+ "제목과 내용이 반드시 입력값이 있어야 임시저장이 가능합니다."),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.NOT_AUTHORIZED_WRITE_NOTICE),
		}
	)
	ResponseEntity<ResponseBody<BoardInfoResponse>> saveDraftSeminar(@Parameter(hidden = true) Long userId,
		@RequestBody @Valid BoardCreateRequest boardCreateRequest);

	@Operation(
		summary = "게시글 상세 조회",
		description = "게시글 상세 정보를 조회합니다."
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoardInfoResponse.class)))
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = BoardInfoResponse.class,
			description = "게시글 상세 조회 성공\n"
				+ "게시글 상세 정보를 반환합니다."),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.NOT_ACCESS_USER),
		}
	)
	ResponseEntity<ResponseBody<BoardInfoResponse>> search( @PathVariable Long boardId);

	@Operation(
		summary = "게시글 수정",
		description = "게시글을 수정합니다."
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoardInfoResponse.class)))
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = BoardInfoResponse.class,
			description = "게시글 수정 성공\n"
				+ "게시글 수정 결과를 반환합니다."),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.NOT_ACCESS_USER),
		}
	)
	ResponseEntity<ResponseBody<BoardInfoResponse>> update(@Parameter(hidden = true) Long userId,
		@RequestBody @Valid BoardUpdateRequest boardUpdateRequest);

	@Operation(
		summary = "게시글 삭제",
		description = "게시글을 삭제합니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			description = "게시글 삭제 성공\n"
				+ "게시글 삭제 결과를 반환합니다."),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.NOT_ACCESS_USER),
		}
	)
	ResponseEntity<ResponseBody<Void>> delete(@Parameter(hidden = true) Long userId, @PathVariable Long boardId);

	@Operation(
		summary = "게시글 목록 조회",
		description = "게시글 목록을 조회합니다."
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalPageResponse.class))
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoardTitleInfoResponse.class))
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = GlobalPageResponse.class,
			description = "게시글 목록 조회 성공\n"
				+ "게시글 목록을 반환합니다.")
	)
	ResponseEntity<ResponseBody<GlobalPageResponse<BoardTitleInfoResponse>>> findBoardPageList(
		@Schema(name="boardType",description = "게시물 타입 SEMINAR/NOTICE 중에 선택해야 합니다.",example = "SEMINAR") BoardType boardType,
		@ParameterObject
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);

	@Operation(
		summary = "임시 저장 게시글 목록 조회",
		description = "해당 사용자가 작성한 임시 저장 게시글 목록을 조회합니다."
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalPageResponse.class)))
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = DraftBoardTitleResponse.class)))
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = GlobalPageResponse.class,
			description = "임시 저장 게시글 목록 조회 성공\n"
				+ "해당 사용자가 작성한 임시 저장 게시글 목록을 반환합니다.")
	)
	ResponseEntity<ResponseBody<GlobalPageResponse<DraftBoardTitleResponse>>> findDraftBoardPageList(
		@Parameter(hidden = true) Long userId,
		@ParameterObject Pageable pageable);


	@Operation(
		summary = "내가 작성한 게시글 목록 조회",
		description = "해당 사용자가 작성한 게시글 목록을 조회합니다. 공개 게시글만 조회됩니다."
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalPageResponse.class)))
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoardTitleInfoResponse.class)))
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = GlobalPageResponse.class,
			description = "내가 작성한 게시글 목록 조회 성공\n"
				+ "해당 사용자가 작성한 게시글 목록을 반환합니다."),

		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
		}
	)
	ResponseEntity<ResponseBody<GlobalPageResponse<BoardTitleInfoResponse>>> findMyBoardPageList(
		@Parameter(hidden = true) Long userId,
		@Schema(name="boardType",description = "게시물 타입 SEMINAR/NOTICE 중에 선택해야 합니다.",example = "SEMINAR") BoardType boardType,
		@ParameterObject Pageable pageable);

}
