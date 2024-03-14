package com.example.demo.domain.file.domain.util;

import java.util.Arrays;
import java.util.List;

public class FileUtil {
    // 이미지 파일 확장자 목록
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "svg"
    );

    // 파일 이름을 기반으로 이미지 파일인지 확인하는 메서드
    public static boolean isImageFile(String fileName) {
        // 파일 이름에서 마지막 '.'의 위치를 찾음
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            // 확장자 추출 (점 다음의 문자열)
            String extension = fileName.substring(dotIndex + 1).toLowerCase();
            // 확장자가 이미지 확장자 목록에 포함되어 있는지 확인
            return IMAGE_EXTENSIONS.contains(extension);
        }
        return false; // 확장자가 없는 경우 이미지 파일이 아님
    }
}
