package com.example.demo.domain.file.domain;

import com.example.demo.domain.file.domain.entity.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileNameInfo {


    private String originalFileName;
    private String storeFileName;


    public FileNameInfo(String originalFileName, String storeFileName) {
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
    }

    public static FileNameInfo from(File file) {
        return new FileNameInfo(file.getOrigin_name(), file.getStored_name());
    }
}
