package com.example.demo.domain.file.domain;


import com.example.demo.domain.announcement.domain.Announcement;
import com.example.demo.domain.comment.domain.Comment;
import com.example.demo.domain.post.domain.Post;
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
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "파일이름이 누락되었습니다.")
    private String uploadFileName;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "파일이름이 누락되었습니다.")
    private String storeFileName;


    @ManyToOne
    @JoinColumn(name ="post_id",nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name ="announcement_id",nullable = true)
    private Announcement announcement;

    @ManyToOne
    @JoinColumn(name ="comment_id",nullable = true)
    private Comment comment;

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
