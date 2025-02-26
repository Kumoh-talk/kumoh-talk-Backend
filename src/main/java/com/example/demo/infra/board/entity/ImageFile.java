package com.example.demo.infra.board.entity;


import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name ="image_files")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE image_files SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class ImageFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 500)
    @NotBlank(message = "url이 누락되었습니다.")
    private String url;

    @ManyToOne
    @JoinColumn(name ="board_id",nullable = true)
    private Board board;

    private ImageFile(String url){
        this.url = url;
    }

    public static ImageFile of(String url,Board board){
        ImageFile imageFile = new ImageFile(url);
        board.getImageFiles().add(imageFile);
        imageFile.board = board;
        return imageFile;
    }
}
