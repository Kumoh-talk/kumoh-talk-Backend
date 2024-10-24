package com.example.demo.domain.board.domain.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="image_files")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE image_files SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class HeadImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false,length = 45)
	private String url;

	@ManyToOne
	@JoinColumn(name ="board_id",nullable = true)
	private Board board;
}
