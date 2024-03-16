package com.example.demo.domain.file.domain;

import com.example.demo.domain.file.domain.entity.UploadFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileNameInfo {


    private String originalFileName;
    private String storeFileName;
    private String url;

    public FileNameInfo(String originalFileName, String storeFileName, String url) {
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
        this.url = url;
    }

    public static FileNameInfo from(UploadFile uploadFile) {
        return new FileNameInfo(uploadFile.getUploadFileName(), uploadFile.getStoreFileName(),uploadFile.getUrl());
    }
}
