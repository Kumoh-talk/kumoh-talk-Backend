package com.example.demo.domain.file.controller;


import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.FileType;
import com.example.demo.domain.file.service.FileService;
import com.example.demo.global.base.dto.ResponseBody;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileController {

    private final FileService fileService;


    @PostMapping("/v1/images/{boardId}")
    public ResponseBody<FileNameInfo> uploadImage(@PathVariable Long boardId,
                               MultipartFile multipartFile) throws IOException {
        return createSuccessResponse(fileService.upload(multipartFile, boardId));
    }
    @PostMapping("/v1/attachs/{boardId}")
    public ResponseBody<FileNameInfo> uploadAttach(@PathVariable Long boardId,
                               MultipartFile multipartFile) throws IOException {
        return createSuccessResponse(fileService.upload(multipartFile, boardId));
    }


}
