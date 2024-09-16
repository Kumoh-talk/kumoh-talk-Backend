package com.example.demo.domain.user.domain;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.Like;
import com.example.demo.domain.newsletter.domain.Newsletter;
import com.example.demo.domain.seminar_application.domain.SeminarApplication;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user_addtional_info.domain.UserAdditionalInfo;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.global.oauth.user.OAuth2Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    @Column(nullable = false)
    private String providerId;

    @Column(unique = true)
    private String nickname;

    private String name;

    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_additional_info_id")
    private UserAdditionalInfo userAdditionalInfo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "news_letter_id")
    private Newsletter newsletter;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Like> likes= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeminarApplication> seminarApplications = new ArrayList<>();

    @Builder
    public User(OAuth2Provider provider, String providerId, String nickname, Role role) {
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.role = role;
    }

    public void setInitialInfo(String nickname, String name) {
        this.nickname = nickname;
        this.name = name;
        this.profileImageUrl = "기본이미지 url";
        this.role = Role.ROLE_USER;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void mapAdditionalInfo(UserAdditionalInfo userAdditionalInfo) {
        this.userAdditionalInfo = userAdditionalInfo;
    }

    public void mapNewsletter(Newsletter newsletter) {
        this.newsletter = newsletter;
    }

    public void addSeminarApplications(SeminarApplication seminarApplication) {
        this.seminarApplications.add(seminarApplication);
    }

    public void updateUserRoleToActiveUser() {
        this.role = Role.ROLE_ACTIVE_USER;
    }

    public void updateUserRoleToSeminarWriter() {
        this.role = Role.ROLE_SEMINAR_WRITER;
    }

    public void changeProfileUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
