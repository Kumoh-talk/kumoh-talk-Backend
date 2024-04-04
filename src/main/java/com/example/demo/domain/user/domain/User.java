package com.example.demo.domain.user.domain;

import com.example.demo.domain.comment.domain.Comment;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.like.domain.Like;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user.domain.vo.Status;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;
import static com.example.demo.global.regex.UserRegex.NAME_REGEXP;

@Slf4j
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "이메일은 빈값 일 수 없습니다.")
    @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "이름은 빈값 일 수 없습니다.")
    @Pattern(regexp = NAME_REGEXP, message = "이름 형식이 맞지 않습니다.")
    private String name;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "이름은 빈값 일 수 없습니다.")
    @Pattern(regexp = NAME_REGEXP, message = "이름 형식이 맞지 않습니다.")
    private String nickname;

    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 빈값 일 수 없습니다.")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Column(nullable = false, length = 10)
    private String department;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status; // 재학 상태

    @Column(nullable = false)
    @NotBlank(message = "필드 값은 빈값 일 수 없습니다.")
    private String field;

    @OneToMany(mappedBy = "user")
    private List<Board> boards;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Like> likes;

    @Builder
    public User(String email, String name, String nickname, String password, Role role, String department, Status status, String field) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.department = department;
        this.status = status;
        this.field = field;
    }

    public void updateInfo(User user) {
        if (user == null) {
            log.warn("UPDATE_FAILED: Invalid user data provided.");
            throw new ServiceException(ErrorCode.INVALID_INPUT_VALUE);
        }
        this.name = user.getName();
//        this.track = user.getTrack();
//        this.major = user.getMajor();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
