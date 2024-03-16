package com.example.demo.domain.file;


import com.example.demo.domain.file.uploader.FileSysStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileSysStore fileUploader;
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws
            MalformedURLException {
        return ResponseEntity.ok(new UrlResource("file:" + fileUploader.getFullDir(filename)));
    }

    @GetMapping("/attach/{filename}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable String filename)
            throws MalformedURLException {
        UrlResource resource = new UrlResource("file:" + fileUploader.getFullDir(filename));
        String contentDisposition = "attachment; filename=\"" + filename + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
