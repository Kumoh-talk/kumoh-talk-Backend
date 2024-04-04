package com.example.demo.domain.file.uploader;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.FileType;
import com.example.demo.domain.file.domain.entity.File;
import com.example.demo.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class FileS3Uploader  {
    private final FileRepository fileRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")                                                        //bucket 이름
    public String bucket;

    private void check(Object saved) {
        if (!(saved instanceof Board)) {  // 파일 업로더가 다른 객체가 추가되면 여기서 클래스 추가하면 됨
            throw new IllegalArgumentException("saved 객체는 알 수 없는 타입입니다.");
        }
    }

    /**
     * 이미지 파일 저장하는 메서드
     * @param multipartFiles ( 이미지 파일들 )
     * @param saved
     * @return List<FileNameInfo>
     * @throws IOException
     */
    public List<FileNameInfo> imageStore(List<MultipartFile> multipartFiles,  Object saved) throws IOException {
        List<FileNameInfo> result = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                result.add(storeFile(multipartFile, saved,FileType.IMAGE));
            }
        }
        return result;
    }

    /**
     * Attach 파일 저장하는 메서드
     * @param multipartFile
     * @param saved
     * @return FileNameInfo
     * @throws IOException
     */
    public FileNameInfo attachStore(MultipartFile multipartFile, Object saved) throws IOException { 

        check(saved); // 저장 가능한 엔티티인지 체크

        return storeFile(multipartFile, saved,FileType.ATTACH);
    }

    private FileNameInfo storeFile(MultipartFile multipartFile, Object saved,FileType fileType) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename(); // 실제 이름
        String storeFileName = createStoreFileName(originalFilename); // 파일 식별 고유 이름 생성

        uploadS3File(multipartFile, storeFileName); // s3 업로드

        File file = new File(originalFilename, storeFileName, fileType);
        file.setEntity(saved); // 연관관계 매핑


        return FileNameInfo.from(fileRepository.save(file));
    }

    //S3 서버에 파일 업로드
    public void uploadS3File(MultipartFile multipartFile, String storeFileName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getInputStream().available());

        amazonS3Client.putObject(bucket, storeFileName, multipartFile.getInputStream(), objectMetadata);
    }


    // TODO : 만약 소프트 delete 한다면 파일 보관 생각해야함
    public void deleteAllFiles(Board board) {
        board.getFiles().forEach(uploadFile -> {
            fileRepository.delete(uploadFile);
            deleteFile(uploadFile.getStored_name()); // s3 파일 삭제 메서드
        });
    }

    /**
     * 게시물의 파일 이름 전부 반환 TODO: N+1 문제 해결 해야함
     * @param board
     * @return
     */
    public List<FileNameInfo> getFiles(Board board) {
        List<FileNameInfo> result = new ArrayList<>();
        board.getFiles().forEach(uploadFile -> {
            result.add(FileNameInfo.from(uploadFile));
        });
        return result;
    }

    public void deleteFile(String storeFileName) {
        amazonS3Client.deleteObject(bucket, storeFileName);
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

    public FileNameInfo getAttachFileName(List<File> files) {
        final FileNameInfo[] fileNameInfo = new FileNameInfo[1];
        files.forEach(file -> {
            if (file.getType() == FileType.ATTACH) {
                fileNameInfo[0] =  FileNameInfo.from(file);

            }
        });
        return fileNameInfo[0];

    }

    public List<FileNameInfo> getImagesFilesName(List<File> files) {
        List<FileNameInfo> fileNameInfos = new ArrayList<>();

        files.forEach(file -> {
            fileNameInfos.add(FileNameInfo.from(file));
        });
        return fileNameInfos;
    }
}
