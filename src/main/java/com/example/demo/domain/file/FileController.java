package com.example.demo.domain.file;


import com.amazonaws.services.s3.AmazonS3Client;
import com.example.demo.domain.file.uploader.FileS3Uploader;
import com.example.demo.domain.file.uploader.FileSysStore;
import com.example.demo.domain.file.uploader.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileS3Uploader fileUploader;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")                                                        //bucket 이름
    public String bucket;

//    @GetMapping("/images/{filename}")
//    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws
//            MalformedURLException {
//        return ResponseEntity.ok(new UrlResource("file:" + fileUploader.getFullDir(filename)));
//    }
//
//    @GetMapping("/attach/{filename}")
//    public ResponseEntity<Resource> downloadAttach(@PathVariable String filename)
//            throws MalformedURLException {
//        UrlResource resource = new UrlResource("file:" + fileUploader.getFullDir(filename));
//        String contentDisposition = "attachment; filename=\"" + filename + "\"";
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(resource);
//    }

//    @PostMapping("/test")
//    public ResponseEntity<String> test(@RequestPart("file") MultipartFile file) throws IOException {
//
//        String url = fileUploader.uploadS3File(file, "1234.png");
//        return ResponseEntity.ok(url);}
}
