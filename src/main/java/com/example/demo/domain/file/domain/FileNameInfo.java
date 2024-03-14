package com.example.demo.domain.file.domain;

import com.example.demo.domain.file.domain.entity.UploadFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileNameInfo {
    private String originalFileName;
    private String storeFileName;

    public FileNameInfo(String originalFileName, String storeFileName) {
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
    }

    public static FileNameInfo from(UploadFile uploadFile) {
        return new FileNameInfo(uploadFile.getUploadFileName(), uploadFile.getStoreFileName());
    }
}
