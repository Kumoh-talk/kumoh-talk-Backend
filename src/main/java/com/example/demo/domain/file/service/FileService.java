package com.example.demo.domain.file.service;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.FileType;
import com.example.demo.domain.file.domain.entity.File;
import com.example.demo.domain.file.repository.FileRepository;
import com.example.demo.domain.file.uploader.FileS3Uploader;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final BoardRepository boardRepository;
    private final FileS3Uploader fileS3Uploader;

    @Transactional
    public FileNameInfo upload(MultipartFile multipartFile, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
            new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        File file = fileS3Uploader.storeFile(multipartFile, board);
        file.setBoard(board); // 연관관계 매핑
        return FileNameInfo.from(fileRepository.save(file));
    }
}
