package com.example.demo.domain.file.domain.entity;


import com.example.demo.domain.announcement.domain.Announcement;
import com.example.demo.domain.board.domain.Board;
import com.example.demo.domain.comment.domain.Comment;
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

    @Column(nullable = false,length = 45)
    @NotBlank(message = "파일이름이 누락되었습니다.")
    private String url;

    @ManyToOne
    @JoinColumn(name ="post_id",nullable = true)
    private Board board;

    @ManyToOne
    @JoinColumn(name ="announcement_id",nullable = true)
    private Announcement announcement;

    @ManyToOne
    @JoinColumn(name ="comment_id",nullable = true)
    private Comment comment;


    public UploadFile(String uploadFileName, String storeFileName, String url) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.url = url;
    }

    public void setEntity(Object saved) {
        if (saved instanceof Board) {
            this.board = (Board) saved;
        } else if (saved instanceof Announcement) {
            this.announcement = (Announcement) saved;
        } else if (saved instanceof Comment) {
            this.comment = (Comment) saved;
        }else {
            throw new IllegalArgumentException("saved 객체는 알 수 없는 타입입니다.");
        }
    }
}
