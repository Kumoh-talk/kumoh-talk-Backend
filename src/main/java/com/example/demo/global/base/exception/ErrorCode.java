package com.example.demo.global.base.exception;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.comment.domain.vo.CommentTargetBoardType;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.domain.recruitment_board.domain.vo.QuestionType;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0001", "잘못된 입력 값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0002", "잘못된 타입입니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0003", "인자가 부족합니다."),
    NOT_EXIST_API(HttpStatus.BAD_REQUEST, "COMMON_0004", "요청 주소가 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_0005", "사용할 수 없는 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_0006", "서버 에러입니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "COMMON_0007", "JSON 파싱 오류입니다."),
    REQUEST_PARAM_MISMATCH(HttpStatus.BAD_REQUEST, "COMMON_0008", "쿼리 파라미터 값이 형식에 맞지 않습니다."),

    // Security
    NEED_AUTHORIZED(HttpStatus.UNAUTHORIZED, "SECURITY_0001", "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "SECURITY_0002", "권한이 없습니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "SECURITY_0003", "JWT 토큰이 만료되었습니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "SECURITY_0004", "JWT 토큰이 올바르지 않습니다."),
    JWT_NOT_EXIST(HttpStatus.UNAUTHORIZED, "SECURITY_0005", "JWT 토큰이 존재하지 않습니다."),

    // Token
    REFRESH_TOKEN_NOT_EXIST(HttpStatus.NOT_FOUND, "TOKEN_0001", "REFRESH 토큰이 존재하지 않습니다."),
    TOKEN_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "TOKEN_0002", "일치하지 않는 토큰입니다."),

    // Auth
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_0002", "비밀번호가 일치하지 않습니다."),
    USERID_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_0003", "존재하지 않는 아이디입니다."),
    EXIST_SAME_USERID(HttpStatus.CONFLICT, "AUTH_0004", "이미 사용중인 아이디 입니다."),
    EXIST_SAME_NICKNAME(HttpStatus.CONFLICT, "AUTH_0005", "이미 사용중인 닉네임 입니다."),
    ADDITIONAL_INFO_NOT_UPDATED(HttpStatus.UNAUTHORIZED, "AUTH_0006", "추가정보가 업데이트되지 않았습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_0001", "해당 사용자는 존재하지 않는 사용자입니다."),
    USER_ADDITIONAL_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_0002", "해당 사용자의 추가정보가 존재하지 않습니다."),
    USER_ADDITIONAL_INFO_CONFLICT(HttpStatus.CONFLICT, "USER_0003", "해당 사용자의 추가정보는 이미 존재합니다."),

    // Board
    NOT_ACCESS_USER(HttpStatus.UNAUTHORIZED, "BOARD_0001", "해당 유저가 접근할 수 없는 게시물입니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_0002", "존재하지 않는 게시물입니다."),
    USER_ALREADY_LIKE_BOARD(HttpStatus.CONFLICT, "BOARD_0003", "이미 좋아요를 누른 게시물입니다."),
    LIKE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_0004", "좋아요는 인증을 해야합니다."),
    USER_NOT_LIKE_BOARD(HttpStatus.NOT_FOUND, "BOARD_0005", "좋아요를 누르지 않은 게시물입니다."),
    DEADLINE_EXPIRED(HttpStatus.FORBIDDEN, "BOARD_0006", "신청 마감된 게시물입니다."),
    NOT_NOTICE_ROLE(HttpStatus.UNAUTHORIZED, "BOARD_0007", "해당 유저는 공지사항 작성 권한이 없습니다."),
    NOT_SEMINAR_ROLE(HttpStatus.UNAUTHORIZED, "BOARD_0008", "해당 유저는 세미나 작성 권한이 없습니다."),
    NOT_AUTHORIZED_WRITE_NOTICE(HttpStatus.UNAUTHORIZED, "BOARD_0009", "공지사항 작성 권한이 없습니다."),
    DRAFT_NOT_ACCESS_USER(HttpStatus.UNAUTHORIZED, "BOARD_0010", "해당 유저는 임시저장된 게시물에 접근할 수 없습니다."),

    // FILE
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "FILE_0001", "파일의 용량이 너무 큽니다."),
    FILE_URL_NOT_MATCHED(HttpStatus.BAD_REQUEST, "FILE_0002", "파일 url 형식이 일치하지 않습니다."),
    NOT_FOUND_IMAGE_FILE(HttpStatus.NOT_FOUND, "FILE_0003", "해당 이미지 파일을 찾을 수 없습니다."),

    // Comment
    PARENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_0001", "부모 댓글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_0002", "해당 id의 댓글을 찾을 수 없습니다."),

    // Newsletter
    SUBSCRIBE_EMAIL_CONFLICT(HttpStatus.CONFLICT, "NEWSLETTER_0001", "이미 구독되어있는 이메일입니다."),
    SUBSCRIBE_NOT_FOUND(HttpStatus.NOT_FOUND, "NEWSLETTER_0002", "구독 정보가 존재하지 않습니다."),
    ALREADY_SUBSCRIBE(HttpStatus.CONFLICT, "NEWSLETTER_0003", "사용자는 이미 구독중입니다."),

    // SeminarApplication
    SEMINAR_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "SEMINAR_0001", "해당 세미나 신청폼 정보가 존재하지 않습니다."),
    SEMINAR_APPLICATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "SEMINAR_0002", "해당 세미나 신청폼에 접근할 권한이 없습니다."),
    SEMINAR_APPLICATION_CANNOT_EDIT_OR_DELETE(HttpStatus.FORBIDDEN, "SEMINAR_0003", "해당 세미나 신청폼을 수정/삭제할 수 있는 기간이 아닙니다."),

    // RecruitmentBoard
    RECRUITMENT_APPLICATION_EXIST(HttpStatus.BAD_REQUEST, "STUDYPROEJCT_0001", "게시물에 신청한 사람이 존재합니다."),

    // RecruitmentFormQuestion
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION_0001", "질문 정보가 존재하지 않습니다."),

    // RecruitmentFormAnswer
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "FORM_ANSWER_0001", "선택한 객관식 답안을 찾을 수 없습니다."),

    // RecruitmentApplicant
    RECRUITMENT_APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLICANT_0001", "신청자 정보가 존재하지 않습니다."),
    RECRUITMENT_APPLICANT_EXIST(HttpStatus.CONFLICT, "APPLICANT_0002", "이미 신청한 사용자가 존재합니다."),

    // RecruitmentApplicantAnswer
    OMIT_ESSENTIAL_QUESTION(HttpStatus.BAD_REQUEST, "APPLICANT_ANSWER_0001", "필수 답변을 입력해야합니다."),

    // Notification
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION_0001", "알림 정보가 존재하지 않습니다."),
    RECEIVER_NOT_MATCH(HttpStatus.NOT_FOUND, "NOTIFICATION_0002", "알림을 받을 사용자가 일치하지 않습니다."),

    // Enum Validation
    ENTIRE_BOARD_TYPE_PARSE_ERROR(HttpStatus.BAD_REQUEST, "BODY_ENUM_0001", EntireBoardType.errorMsg),
    QUESTION_TYPE_PARSE_ERROR(HttpStatus.BAD_REQUEST, "BODY_ENUM_0002", QuestionType.errorMsg),
    RECRUITMENT_BOARD_TAG_PARSE_ERROR(HttpStatus.BAD_REQUEST, "BODY_ENUM_0003", RecruitmentBoardTag.errorMsg),
    RECRUITMENT_BOARD_TYPE_PARSE_ERROR(HttpStatus.BAD_REQUEST, "BODY_ENUM_0004", RecruitmentBoardType.errorMsg),
    COMMENT_TARGET_BOARD_TYPE_PARSE_ERROR(HttpStatus.BAD_REQUEST, "BODY_ENUM_0005", CommentTargetBoardType.errorMsg),
    STATUS_PARSE_ERROR(HttpStatus.BAD_REQUEST, "BODY_ENUM_0005", Status.errorMsg),
    BOARD_TYPE_PARSE_ERROR(HttpStatus.BAD_REQUEST, "BODY_ENUM_0006", BoardType.errorMsg),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
