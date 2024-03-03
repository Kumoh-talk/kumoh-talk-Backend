
package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserInfoResponse {
    private Long id;
    private String name;
    private String email;
    private Track track;
    private String major;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.track = user.getTrack();
        this.major = user.getMajor();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

}
