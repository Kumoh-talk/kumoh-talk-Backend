package com.example.demo.domain.board.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.board.domain.entity.HeadImage;

@Repository
public interface HeadImageRepository extends JpaRepository<HeadImage, Long> {
}
