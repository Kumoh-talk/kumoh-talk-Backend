package com.example.demo.domain.user.domain;

import com.example.demo.domain.user.domain.vo.StudentStatus;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_additional_info")
@SQLDelete(sql = "UPDATE user_additional_info SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class UserAdditionalInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String department; // 정해져있음.
    private int studentId;
    private int grade;
    private StudentStatus studentStatus;
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
