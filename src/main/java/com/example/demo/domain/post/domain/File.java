package com.example.demo.domain.post.domain;


import com.example.demo.domain.announcement.domain.Announcement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="file")
@NoArgsConstructor
@Getter
@Setter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "파일이름이 누락되었습니다.")
    private String filename;

    @Lob
    @Column(nullable = false)
    @NotBlank(message = "파일에 데이터가 없습니다.")
    private byte[] file;


    @ManyToOne
    @JoinColumn(name ="post_id",nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name ="announcement_id",nullable = true)
    private Announcement announcement;







}
