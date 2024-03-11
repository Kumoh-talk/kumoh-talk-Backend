package com.example.demo.domain.announcement.domain;


import com.example.demo.domain.post.domain.File;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name ="announcement")
@NoArgsConstructor
@Getter
@Setter
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    private String title;

    @Column(nullable = false,length = 500)
    @NotBlank(message = "게시물은 빈 값일 수 없습니다.")
    private String contents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Track track;

    @ManyToOne
    @JoinColumn(name ="user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;
}
