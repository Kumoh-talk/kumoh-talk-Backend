package com.example.demo.domain.board.service;

import com.example.demo.domain.board.Repository.ApplicationBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationBoardService {
    private final ApplicationBoardRepository applicationBoardRepository;
}
