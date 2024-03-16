package com.example.demo.domain.file.uploader;

import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.post.domain.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileUploader {
    List<FileNameInfo> storeFiles(List<MultipartFile> multipartFiles, Post post) throws IOException;

    FileNameInfo storeFile(MultipartFile multipartFile, Post savedPost) throws IOException;
    void deletePostFiles(Post post);
    void deleteFile(String storeFileName);
    List<FileNameInfo> getPostFiles(Post post);

    default String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    default String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
