package com.example.demo.global.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.domain.board.domain.dto.request.PresignedUrlRequest;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import jakarta.annotation.PostConstruct;

@Component
public class S3UrlUtil {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private static final String BOARD_S3_PATH_FORMAT = "board/%s/%s/%s/%s";
	private static final String BOARD_URL_PATTERN = "^https://%s\\.s3\\.ap-northeast-2\\.amazonaws\\.com/%s/([\\w-]+)/([\\w-]+)/([\\w-]+)/([\\w.-]+)$";
	private static final String BOARD_DOMAIN_NAME = "board";
	private static Pattern boardS3UrlPattern;

	@PostConstruct
	public void init() {
		String urlPattern = String.format(BOARD_URL_PATTERN, bucket, BOARD_DOMAIN_NAME);
		boardS3UrlPattern = Pattern.compile(urlPattern);
	}


	/**
	 * S3 컨벤션에 맞는 경로 설정을 위한 메서드입니다.
	 * Board 에 관한 S3 경로를 생성합니다.
	 * @param presignedUrlRequest
	 * @return Board 관련 S3 경로 문자열
	 */
	public String generateBoardS3Path(PresignedUrlRequest presignedUrlRequest) {
		return String.format(BOARD_S3_PATH_FORMAT,
			presignedUrlRequest.getBoardId(),
			changeLowerCase(presignedUrlRequest.getFileType().toString()),
			creaeteUUID(),
			presignedUrlRequest.getFileName());
	}

	/**
	 * S3 URL을 검증하는 메서드입니다.
	 * @param url
	 */
	public void validateBoardS3Url(String url) {
		// 입력된 URL과 패턴을 매칭합니다.

		Matcher matcher = boardS3UrlPattern.matcher(url);
		if(!matcher.matches()){
			throw new ServiceException(ErrorCode.FILE_URL_NOT_MATCHED);
		}
	}
	private String creaeteUUID() {
		return UUID.randomUUID().toString();
	}

	private String changeLowerCase(String string) {
		return string.toLowerCase();
	}

}
