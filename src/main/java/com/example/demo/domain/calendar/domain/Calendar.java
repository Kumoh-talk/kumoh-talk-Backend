package com.example.demo.domain.calendar.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "calendars")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "제목은 빈값 일 수 없습니다.")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "내용은 빈값 일 수 없습니다.")
    private String contents;

    private Date date;
}
