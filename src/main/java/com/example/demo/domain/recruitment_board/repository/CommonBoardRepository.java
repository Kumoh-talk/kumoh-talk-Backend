package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;

import java.util.Optional;

public interface CommonBoardRepository {
    Optional<GenericBoard> doFindById(Long id);
}
