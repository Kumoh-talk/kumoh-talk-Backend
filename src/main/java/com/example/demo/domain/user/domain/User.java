package com.example.demo.domain.user.domain;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.Like;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.oauth.user.OAuth2Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private OAuth2Provider provider;

    @Column(nullable = false, unique = true)
    private String providerId;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private UserAdditionalInfo userAdditionalInfo;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Like> likes= new ArrayList<>();

    @Builder
    public User(OAuth2Provider provider, String providerId, String nickname, Role role) {
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.role = role;
    }



    public void updateInfo(User user) {
        if (user == null) {
            log.warn("UPDATE_FAILED: Invalid user data provided.");
            throw new ServiceException(ErrorCode.INVALID_INPUT_VALUE);
        }
//        this.name = user.getName();
//        this.track = user.getTrack();
//        this.major = user.getMajor();
    }
}
