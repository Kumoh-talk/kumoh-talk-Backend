package com.example.demo.domain.user.dto.request;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.example.demo.global.regex.UserRegex.NAME_REGEXP;

@Getter
public class UserUpdateRequest {
    @NotBlank(message = "이름은 빈값 일 수 없습니다.")
    @Pattern(regexp = NAME_REGEXP, message = "이름 형식이 맞지 않습니다.")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Track track;

    @Column(nullable = false)
    @NotBlank(message = "전공은 빈값 일 수 없습니다.")
    private String major;

    public static User toUser(UserUpdateRequest request) {
        return User.builder()
                .name(request.getName())
                .track(request.getTrack())
                .major(request.getMajor())
                .build();
    }
}
