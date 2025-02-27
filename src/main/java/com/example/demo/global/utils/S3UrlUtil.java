package com.example.demo.global.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.application.board.dto.request.PresignedUrlRequest;
import com.example.demo.domain.board.service.entity.BoardFileInfo;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import jakarta.annotation.PostConstruct;

@Component
public class S3UrlUtil {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Getter
	@Value("${cloud.aws.s3.default-image-url}")
	private String defaultImageUrl;

	private static final String BOARD_S3_PATH_FORMAT = "board/%s/%s/%s/%s";
	private static final String PROFILE_S3_PATH_FORMAT = "profile/%s/%s/%s/%s";
	private static final String URL_PATTERN = "^https://%s\\.s3\\.ap-northeast-2\\.amazonaws\\.com/%s/([\\w-]+)/([\\w-]+)/([\\w-]+)/([\\w.-]+)$";
	private static final String BOARD_DOMAIN_NAME = "board";
	private static final String PROFILE_DOMAIN_NAME = "profile";

	private static Pattern boardS3UrlPattern;
	private static Pattern profileS3UrlPattern;

	@PostConstruct
	public void init() {
		String boardUrlPattern = String.format(URL_PATTERN, bucket, BOARD_DOMAIN_NAME);
		boardS3UrlPattern = Pattern.compile(boardUrlPattern);
		String profileUrlPattern = String.format(URL_PATTERN, bucket, PROFILE_DOMAIN_NAME);
		profileS3UrlPattern = Pattern.compile(profileUrlPattern);
	}


	/**
	 * S3 컨벤션에 맞는 경로 설정을 위한 메서드입니다.
	 * Board 에 관한 S3 경로를 생성합니다.
	 * @param boardId
	 * @param boardFileInfo
	 * @return Board 관련 S3 경로 문자열
	 */
	public String generateBoardS3Path(Long boardId, BoardFileInfo boardFileInfo) {
		return String.format(BOARD_S3_PATH_FORMAT,
			boardId,
			changeLowerCase(boardFileInfo.getFileType().toString()),
			creaeteUUID(),
			boardFileInfo.getFileName());
	}

	/**
	 * S3 컨벤션에 맞는 경로 설정을 위한 메서드입니다.
	 * Profile 에 관한 S3 경로를 생성합니다.
	 * @param userId
	 * @param fileType
	 * @param fileName
	 * @return Profile 관련 S3 경로 문자열
	 */
	public String generateProfileS3Path(Long userId, String fileType, String fileName) {
		return String.format(PROFILE_S3_PATH_FORMAT,
			userId,
			changeLowerCase(fileType),
			creaeteUUID(),
			fileName);
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

	/**
	 * S3 URL을 검증하는 메서드입니다.
	 * @param url
	 */
	public void validateProfileS3Url(String url) {
		// 입력된 URL과 패턴을 매칭합니다.
		Matcher matcher = profileS3UrlPattern.matcher(url);
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
