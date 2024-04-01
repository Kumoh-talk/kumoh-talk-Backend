package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateResponse {
    private String name;
    private Status track;
    private String major;
    private LocalDateTime updatedAt;

    public static UserUpdateResponse from(User user) {
        return new UserUpdateResponse();
    }
}
