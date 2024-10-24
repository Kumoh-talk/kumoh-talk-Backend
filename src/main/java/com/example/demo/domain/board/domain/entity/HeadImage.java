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
@Table(name ="head_images")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE head_images SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class HeadImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false,length = 256)
	private String url;

	@ManyToOne
	@JoinColumn(name ="board_id",nullable = true)
	private Board board;

	private HeadImage(String url, Board board) {
		this.url = url;
		this.board = board;
		this.board.getHeadImages().add(this);
	}

	public static HeadImage createHeadImage(String url, Board board) {
		return new HeadImage(url, board);
	}
}
