package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserUpdateResponse {
    private String name;
    private Track track;
    private String major;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime updatedAt;

    public static UserUpdateResponse from(User user) {
        return new UserUpdateResponse(
                user.getName(),
                user.getTrack(),
                user.getMajor(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
