package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.ApplicationBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationBoardRepository extends JpaRepository<ApplicationBoard, Long> {

}
