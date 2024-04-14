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
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;
import static com.example.demo.global.regex.UserRegex.NAME_REGEXP;

@Slf4j
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String department; // 학과

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status; // 재학 상태

    @Column(nullable = false)
    private String field; // 희망분야

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Like> likes= new ArrayList<>();

    @Builder
    public User(Long id,String email, String name, String nickname, String password, Role role, String department, Status status, String field) {
        this.id = id;
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
