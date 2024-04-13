package com.example.demo.domain.file.controller;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.FileType;
import com.example.demo.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;


    @PostMapping("/image/{boardId}")
    public FileNameInfo uploadImage(@PathVariable Long boardId,
                               MultipartFile multipartFile) throws IOException {
        return fileService.upload(multipartFile, boardId, FileType.IMAGE);
    }
    @PostMapping("/attach/{boardId}")
    public FileNameInfo uploadAttach(@PathVariable Long boardId,
                               MultipartFile multipartFile) throws IOException {
        return fileService.upload(multipartFile, boardId,FileType.ATTACH);
    }


}
