package com.example.demo.domain.file.uploader;


import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.entity.UploadFile;
import com.example.demo.domain.file.domain.util.FileUtil;
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
public class FileSysStore implements FileUploader{

    private final FileRepository fileRepository;

    @Value("${file.dir}")
    private String fileDir;


    public String getFullDir(String filename) {
        return fileDir + filename;
    }



    public List<FileNameInfo> storeFiles(List<MultipartFile> multipartFiles, Post post) throws IOException {
        List<FileNameInfo> result = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                result.add(storeFile(multipartFile, post));
            }
        }
        return result;
    }

    /**
     *  요청으로온 파일 하나를 파일 시스템에 저장하는 메서드
     * @param multipartFile
     * @return 업로드된 파일
     * @throws IOException
     */
    public FileNameInfo storeFile(MultipartFile multipartFile,Post savedPost) throws IOException
    {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullDir(storeFileName))); // 파일 저장

        UploadFile uploadFile = new UploadFile(originalFilename, storeFileName);
        uploadFile.setPost(savedPost);

        return FileNameInfo.from(fileRepository.save(uploadFile)); // DB 에 저장
    }




    /**
     * 게시물 관련  파일들 삭제 메서드
     * @param post
     */
    public void deletePostFiles(Post post) {
        post.getUploadFiles().forEach(uploadFile -> {
            fileRepository.delete(uploadFile);
            deleteFile(uploadFile.getStoreFileName());
        });
    }





    /**
     *  파일 삭제 메서드
     * @param storeFileName
     */
    public void deleteFile(String storeFileName) {
        File file = new File(getFullDir(storeFileName));
        file.delete();
    }

    /**
     * 게시물의 파일 전부 반환
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
