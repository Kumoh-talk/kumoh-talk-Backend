package com.example.demo.domain.file.uploader;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.entity.UploadFile;
import com.example.demo.domain.file.repository.FileRepository;
import com.example.demo.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class FileS3Uploader implements FileUploader{
    private final FileRepository fileRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")                                                        //bucket 이름
    public String bucket;

    public List<FileNameInfo> storeFiles(List<MultipartFile> multipartFiles, Post post) throws IOException {
        List<FileNameInfo> result = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                result.add(storeFile(multipartFile, post));
            }
        }
        return result;
    }

    public FileNameInfo storeFile(MultipartFile multipartFile, Post savedPost) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename(); // 실제 이름
        String storeFileName = createStoreFileName(originalFilename); // 파일 식별 고유 이름

        String uploadUrl = uploadFile(multipartFile, storeFileName);
        UploadFile uploadFile = new UploadFile(originalFilename, storeFileName);
        uploadFile.setPost(savedPost);


        return FileNameInfo.from(fileRepository.save(uploadFile)); // DB 에 저장
    }

    protected String uploadFile(MultipartFile multipartFile, String storeFileName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getInputStream().available());

        amazonS3Client.putObject(bucket, storeFileName, multipartFile.getInputStream(), objectMetadata);
        return amazonS3Client.getUrl(bucket, storeFileName).toString();
    }


    public void deletePostFiles(Post post) {
        post.getUploadFiles().forEach(uploadFile -> {
            fileRepository.delete(uploadFile);
            deleteFile(uploadFile.getStoreFileName());
        });
    }

    public void deleteFile(String storeFileName) {
        amazonS3Client.deleteObject(bucket, storeFileName);
    }

    public List<FileNameInfo> getPostFiles(Post post) {
        return null;
    }

}
