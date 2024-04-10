package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<View,Long> {
}
