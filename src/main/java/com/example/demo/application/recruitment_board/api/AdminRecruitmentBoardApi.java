package com.example.demo.application.recruitment_board.api;

import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface AdminRecruitmentBoardApi {
    @Operation(
            summary = "관리자 전용 모집 게시물 정보 및 신청폼 삭제",
            description = "관리자가 recruitmentBoardId에 해당하는 모집 게시물 정보 및 신청폼을 삭제합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "관리자가 모집 게시물 정보 및 신청폼 삭제 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<Void>> deleteRecruitmentBoardAndFormByAdmin(@PathVariable Long recruitmentBoardId);
}
