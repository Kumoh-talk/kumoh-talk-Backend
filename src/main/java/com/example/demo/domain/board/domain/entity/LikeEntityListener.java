package com.example.demo.domain.board.domain.entity;

import com.example.demo.domain.board.service.service.LikeNotificationService;
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
    private LikeNotificationService likeNotificationService;

    @PreRemove
    public void preRemove(Object comment) {
        if (comment instanceof Like like) {
            if (like.getDeletedAt() == null) {
                likeNotificationService.deleteLikeNotification(like.getId());
            }
        }
    }
}
