package com.example.demo.domain.user.entity;

import com.example.demo.domain.board.service.entity.vo.FileType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfilePresignedUrl {
    String fileName;
    FileType fileType;

    public ProfilePresignedUrl(String fileName, FileType fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
