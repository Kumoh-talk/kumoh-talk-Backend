package com.example.demo.domain.file;


import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.entity.UploadFile;
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


    public String getFullDir(String filename) {
        return fileDir + filename;
    }



    public List<FileNameInfo> storePostFiles(List<MultipartFile> multipartFiles, Post post) throws IOException {
        List<FileNameInfo> result = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                result.add(storeFile(multipartFile, post));
            }
        }
        return result;
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



    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


    public FileNameInfo getPostAttachFile(Post post) {
        return null;
    }

    public List<FileNameInfo> getPostImagesFiles(Post post) {
        return null;
    }
}
