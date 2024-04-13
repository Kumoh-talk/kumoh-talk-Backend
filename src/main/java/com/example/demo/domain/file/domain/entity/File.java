package com.example.demo.domain.file.domain.entity;


import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.file.domain.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
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
    private String origin_name;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "파일이름이 누락되었습니다.")
    private String stored_name;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    private FileType type;



    @ManyToOne
    @JoinColumn(name ="post_id",nullable = true)
    private Board board;


    @Builder
    public File(String origin_name, String stored_name, FileType type) {
        this.origin_name = origin_name;
        this.stored_name = stored_name;
        this.type = type;
    }

    public void setEntity(Object saved) {
        if (saved instanceof Board) {
            this.board = (Board) saved;
        }else {
            throw new IllegalArgumentException("saved 객체는 알 수 없는 타입입니다.");
        }
    }
}
