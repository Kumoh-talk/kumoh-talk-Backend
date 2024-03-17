package com.example.demo.domain.file.uploader;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.domain.announcement.domain.Announcement;
import com.example.demo.domain.comment.domain.Comment;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.entity.UploadFile;
import com.example.demo.domain.file.domain.util.FileUtil;
import com.example.demo.domain.file.repository.FileRepository;
import com.example.demo.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class FileS3Uploader implements FileUploader {
    private final FileRepository fileRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")                                                        //bucket 이름
    public String bucket;

    private void check(Object saved) {
        if (!(saved instanceof Post || saved instanceof Announcement || saved instanceof Comment)) {
            throw new IllegalArgumentException("saved 객체는 알 수 없는 타입입니다.");
        }
    }


    public List<FileNameInfo> storeFiles(List<MultipartFile> multipartFiles,  Object saved) throws IOException {
        List<FileNameInfo> result = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                result.add(storeFile(multipartFile, saved));
            }
        }
        return result;
    }

    public FileNameInfo storeFile(MultipartFile multipartFile, Object saved) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        check(saved);


        String originalFilename = multipartFile.getOriginalFilename(); // 실제 이름
        String storeFileName = createStoreFileName(originalFilename); // 파일 식별 고유 이름

        String uploadUrl = uploadS3File(multipartFile, storeFileName);
        UploadFile uploadFile = new UploadFile(originalFilename, storeFileName,uploadUrl);
        uploadFile.setEntity(saved);


        return FileNameInfo.from(fileRepository.save(uploadFile)); // DB 에 저장
    }

    protected String uploadS3File(MultipartFile multipartFile, String storeFileName) throws IOException {
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

    /**
     * 게시물의 파일 이름 전부 반환
     * @param post
     * @return
     */
    public List<FileNameInfo> getPostFiles(Post post) {
        List<FileNameInfo> result = new ArrayList<>();
        post.getUploadFiles().forEach(uploadFile -> {
            result.add(FileNameInfo.from(uploadFile));
        });
        return result;
    }
    public void deleteFile(String storeFileName) {
        amazonS3Client.deleteObject(bucket, storeFileName);
    }

    public FileNameInfo getPostAttachFile(List<UploadFile> uploadFiles) {
        for (UploadFile uploadFile : uploadFiles) {
            if (!FileUtil.isImageFile(uploadFile.getStoreFileName())) {
                return FileNameInfo.from(uploadFile);
            }
        }
        return null;
    }

    public List<FileNameInfo> getPostImagesFiles(List<UploadFile> uploadFiles) {
        return uploadFiles.stream()
                .filter(uploadFile -> {return FileUtil.isImageFile(uploadFile.getStoreFileName());})
                .map(FileNameInfo::from)
                .toList();
    }

}
