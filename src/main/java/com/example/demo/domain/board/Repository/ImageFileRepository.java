package com.example.demo.domain.board.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.ImageFile;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
	Optional<ImageFile> findByUrlAndBoard(String url, Board board);
}
