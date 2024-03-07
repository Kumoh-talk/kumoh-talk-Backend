package com.example.demo.domain.user.domain;

import com.example.demo.domain.calendar.domain.Calendar;
import com.example.demo.domain.user.domain.vo.Track;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;
import static com.example.demo.global.regex.UserRegex.NAME_REGEXP;

@Slf4j
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    public enum Role {
        USER,
        ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "이름은 빈값 일 수 없습니다.")
    @Pattern(regexp = NAME_REGEXP, message = "이름 형식이 맞지 않습니다.")
    private String name;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "이메일은 빈값 일 수 없습니다.")
    @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 빈값 일 수 없습니다.")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Track track;

    @Column(nullable = false)
    @NotBlank(message = "전공은 빈값 일 수 없습니다.")
    private String major;

    @OneToMany(mappedBy = "user")
    private List<Calendar> calendars;

    @Builder
    public User(Long id, String name, String email, String password, Track track, String major, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.track = track;
        this.major = major;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateInfo(User user) {
        if (user == null) {
            log.warn("UPDATE_FAILED: Invalid user data provided.");
            throw new ServiceException(ErrorCode.INVALID_INPUT_VALUE);
        }
        this.name = user.getName();
        this.track = user.getTrack();
        this.major = user.getMajor();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
