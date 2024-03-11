package com.example.demo.domain.comment.domain.response;

import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@Setter
@AllArgsConstructor
public class CommentInfoResponse {
    private Long commentId;
    private String usernme;
    private String contents;


    public static CommentInfoResponse from(Comment comment) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPrincipal user = (UserPrincipal)principal;
        return new CommentInfoResponse(comment.getId(), user.getName(), comment.getContents());
    }
}
