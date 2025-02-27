package com.example.demo.infra.board.entity;

import com.example.demo.domain.board.service.implement.LikeNotificationHandler;
import jakarta.persistence.PreRemove;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class LikeEntityListener {
    @Lazy
    @Autowired
    private LikeNotificationHandler likeNotificationHandler;

    @PreRemove
    public void preRemove(Object comment) {
        if (comment instanceof Like like) {
            if (like.getDeletedAt() == null) {
                likeNotificationHandler.deleteLikeNotification(like.getId());
            }
        }
    }
}
