package com.example.demo.domain.comment.controller;

import com.example.demo.domain.comment.domain.response.CommentInfo;
import com.example.demo.domain.comment.domain.response.CommentResponse;
import com.example.demo.domain.comment.service.CommentService;
import com.example.demo.global.base.dto.ErrorResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CommentController.class)
@WithMockUser
@ExtendWith(MockitoExtension.class)
@AutoConfigureRestDocs
public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("게시물 댓글 조회 컨트롤러")
    class GetComments{
        String getCommentsUrl = "/api/comments/{boardId}";
        private final Long boardId = 1L;
        private final ErrorResponse inValidInputResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, null);

        private final ErrorResponse notFoundBoardResponse = ErrorResponse.of(ErrorCode.BOARD_NOT_FOUND,null);
        @Test
        @DisplayName("댓글 반환 성공")
        public void success() throws Exception {
            // given
            List<CommentInfo> commentInfoList = TestDataFactory.createCommentInfoList();

            BDDMockito.given(commentService.findByBoardId(any(Long.class)))
                    .willReturn(CommentResponse.from(commentInfoList));

            // when -> then
            mockMvc.perform(RestDocumentationRequestBuilders
                    .get(getCommentsUrl, boardId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.commentsCount").value(commentInfoList.size()))

                    .andExpect(jsonPath("$.commentInfoList[0].commentId").value(commentInfoList.get(0).getCommentId()))
                    .andExpect(jsonPath("$.commentInfoList[0].groupId").value(commentInfoList.get(0).getGroupId()))
                    .andExpect(jsonPath("$.commentInfoList[0].depth").value(commentInfoList.get(0).getDepth()))
                    .andExpect(jsonPath("$.commentInfoList[0].username").value(commentInfoList.get(0).getUsername()))
                    .andExpect(jsonPath("$.commentInfoList[0].contents").value(commentInfoList.get(0).getContents()))
                    .andExpect(jsonPath("$.commentInfoList[0].createdAt").value(commentInfoList.get(0).getCreatedAt().toString()))
                    .andExpect(jsonPath("$.commentInfoList[0].updatedAt").value(commentInfoList.get(0).getUpdatedAt()))
                    .andExpect(jsonPath("$.commentInfoList[0].deletedAt").value(commentInfoList.get(0).getDeletedAt()))

                    .andExpect(jsonPath("$.commentInfoList[1].commentId").value(commentInfoList.get(1).getCommentId()))
                    .andExpect(jsonPath("$.commentInfoList[1].groupId").value(commentInfoList.get(1).getGroupId()))
                    .andExpect(jsonPath("$.commentInfoList[1].depth").value(commentInfoList.get(1).getDepth()))
                    .andExpect(jsonPath("$.commentInfoList[1].username").value(commentInfoList.get(1).getUsername()))
                    .andExpect(jsonPath("$.commentInfoList[1].contents").value(commentInfoList.get(1).getContents()))
                    .andExpect(jsonPath("$.commentInfoList[1].createdAt").value(commentInfoList.get(1).getCreatedAt().toString()))
                    .andExpect(jsonPath("$.commentInfoList[1].updatedAt").value(commentInfoList.get(1).getUpdatedAt()))
                    .andExpect(jsonPath("$.commentInfoList[1].deletedAt").value(commentInfoList.get(1).getDeletedAt()))

                    .andExpect(jsonPath("$.commentInfoList[2].commentId").value(commentInfoList.get(2).getCommentId()))
                    .andExpect(jsonPath("$.commentInfoList[2].groupId").value(commentInfoList.get(2).getGroupId()))
                    .andExpect(jsonPath("$.commentInfoList[2].depth").value(commentInfoList.get(2).getDepth()))
                    .andExpect(jsonPath("$.commentInfoList[2].username").value(commentInfoList.get(2).getUsername()))
                    .andExpect(jsonPath("$.commentInfoList[2].contents").value(commentInfoList.get(2).getContents()))
                    .andExpect(jsonPath("$.commentInfoList[2].createdAt").value(commentInfoList.get(2).getCreatedAt().toString()))
                    .andExpect(jsonPath("$.commentInfoList[2].updatedAt").value(commentInfoList.get(2).getUpdatedAt()))
                    .andExpect(jsonPath("$.commentInfoList[2].deletedAt").value(commentInfoList.get(2).getDeletedAt()))
                    .andDo(document("comment/comment-get-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("boardId").description("게시물 ID")
                            ),
                            responseFields(
                                    fieldWithPath("commentsCount").type(JsonFieldType.NUMBER).description("댓글 수"),

                                    subsectionWithPath("commentInfoList").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                    fieldWithPath("commentInfoList[].commentId").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                    fieldWithPath("commentInfoList[].groupId").type(JsonFieldType.NUMBER).description("댓글 그룹 ID(댓글과 대댓글이 같은 그룹 / 부모 댓글 ID를 그룹 ID로 가짐)"),
                                    fieldWithPath("commentInfoList[].depth").type(JsonFieldType.NUMBER).description("깊이(부모 댓글이면 0, 대댓글이면 1)"),
                                    fieldWithPath("commentInfoList[].username").type(JsonFieldType.STRING).description("작성자"),
                                    fieldWithPath("commentInfoList[].contents").type(JsonFieldType.STRING).description("댓글 내용"),
                                    fieldWithPath("commentInfoList[].likedCount").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                                    fieldWithPath("commentInfoList[].createdAt").type(JsonFieldType.STRING).description("작성일"),
                                    fieldWithPath("commentInfoList[].updatedAt").type(JsonFieldType.STRING).description("수정일").optional(),
                                    fieldWithPath("commentInfoList[].deletedAt").type(JsonFieldType.STRING).description("삭제일(null이 아니라면 작성자, 댓글 내용을 변경)").optional()
                            )
                            ));
        }

        @Test
        @DisplayName("댓글이 존재하지 않는 게시물은 빈 리스트 반환")
        public void commentsEmptySuccess() throws Exception {
            // given
            BDDMockito.given(commentService.findByBoardId(any(Long.class)))
                    .willReturn(new CommentResponse(0, Collections.emptyList()));
            // when -> then
            mockMvc.perform(RestDocumentationRequestBuilders
                    .get(getCommentsUrl, boardId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.commentsCount").value(0));
        }

        @Test
        @DisplayName("유효하지 않은 pathVariable 값")
        public void inValidInputFail() throws Exception {
            // given

            // when -> then
            mockMvc.perform(RestDocumentationRequestBuilders
                    .get(getCommentsUrl, "abc"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(inValidInputResponse.getCode()))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value(inValidInputResponse.getMessage()))
                    .andDo(document("comment/comment-get-InValidInput-fail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("boardId").description("게시물 ID")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").description("에러 발생 시간"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("에러 상태"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("에러 설명")
                            )));
        }

        @Test
        @DisplayName("유효하지 않은 게시물")
        public void notFoundBoardFail() throws Exception {
            // given
            BDDMockito.given(commentService.findByBoardId(any(Long.class))).willThrow(new ServiceException(ErrorCode.BOARD_NOT_FOUND));
            // when -> then
            mockMvc.perform(RestDocumentationRequestBuilders
                    .get(getCommentsUrl, boardId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(notFoundBoardResponse.getCode()))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value(notFoundBoardResponse.getMessage()))
                    .andDo(document("comment/comment-get-BoardNotFound-fail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("boardId").description("게시물 ID")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").description("에러 발생 시간"),
                                    fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("에러 상태"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("에러 설명")
                            )));
        }
    }

    static class TestDataFactory{
        public static List<CommentInfo> createCommentInfoList(){
            List<CommentInfo> commentInfoList = new ArrayList<>();
            commentInfoList.add(
                    CommentInfo.builder()
                            .commentId(0L)
                            .groupId(0L)
                            .depth(0)
                            .username("a")
                            .contents("aaa")
                            .likedCount(0)
                            .createdAt(LocalDateTime.of(2024,1,1,0,0,10))
                            .updatedAt(null)
                            .deletedAt(null)
                            .build()
            );
            commentInfoList.add(
                    CommentInfo.builder()
                            .commentId(1L)
                            .groupId(0L)
                            .depth(1)
                            .username("b")
                            .contents("bbb")
                            .likedCount(1)
                            .createdAt(LocalDateTime.of(2024,1,1,1,0,10))
                            .updatedAt(null)
                            .deletedAt(null)
                            .build()
            );
            commentInfoList.add(
                    CommentInfo.builder()
                            .commentId(2L)
                            .groupId(2L)
                            .depth(0)
                            .username("c")
                            .contents("ccc")
                            .likedCount(5)
                            .createdAt(LocalDateTime.of(2024,1,1,2,0,10))
                            .updatedAt(null)
                            .deletedAt(null)
                            .build()
            );

            return commentInfoList;
        }

        //public CommentRequest createCommentRequest(){}

    }
}
