package com.example.demo.infra.board.Repository;

import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
	Optional<ImageFile> findByUrlAndBoard(String url, Board board);
}
