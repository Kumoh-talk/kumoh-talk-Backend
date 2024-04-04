package com.example.demo.domain.file.uploader;

import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploader {
    List<FileNameInfo> storeFiles(List<MultipartFile> multipartFiles,  Object saved) throws IOException;

    FileNameInfo storeFile(MultipartFile multipartFile, Object saved) throws IOException;
    void deletePostFiles(Board board);
    void deleteFile(String storeFileName);
    List<FileNameInfo> getPostFiles(Board board);

    FileNameInfo getPostAttachFile(List<File> files);
    List<FileNameInfo> getPostImagesFiles(List<File> files);


}
