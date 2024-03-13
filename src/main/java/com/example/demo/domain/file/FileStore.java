package com.example.demo.domain.file;


import com.example.demo.domain.comment.domain.Comment;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.UploadFile;
import com.example.demo.domain.file.repository.FileRepository;
import com.example.demo.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileStore {

    private FileRepository fileRepository;

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<FileNameInfo> storePostFiles(List<MultipartFile> multipartFiles, Post savedPost) throws IOException {
        List<FileNameInfo> result = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = storeFile(multipartFile); // 파일 시스템 저장
                uploadFile.setPost(savedPost); // 연관 관계 생성
                fileRepository.save(uploadFile); // DB 에 저장
                result.add(FileNameInfo.from(uploadFile));
            }
        }
        return result;
    }

    public List<UploadFile> storeCommentFiles(List<MultipartFile> multipartFiles, Comment savedComment) throws IOException {
        List<UploadFile> result = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = storeFile(multipartFile); // 파일 시스템 저장
                uploadFile.setComment(savedComment); // 연관 관계 생성
                fileRepository.save(uploadFile); // DB 에 저장
                result.add(uploadFile);
            }
        }
        return result;
    }




    public UploadFile storeFile(MultipartFile multipartFile) throws IOException
    {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName))); // 파일 저장
        return new UploadFile(originalFilename, storeFileName);
    }
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
