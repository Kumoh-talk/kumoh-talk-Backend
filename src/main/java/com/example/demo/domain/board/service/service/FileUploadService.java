package com.example.demo.domain.board.service.service;

import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.Repository.ImageFileRepository;
import com.example.demo.domain.board.domain.dto.request.FileRequest;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.ImageFile;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadService {
	private static final Integer EXPIRATION_TIME = 1000 * 60 * 2;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3Client amazonS3Client;
	private final ImageFileRepository imageFileRepository;
	private final BoardRepository boardRepository;

	public String generatePresignedUrl(String s3Path) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(s3Path);

		URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

		return url.toString();
	}

	private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String s3Path) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, s3Path)
			.withMethod(HttpMethod.PUT)
			.withExpiration(getPresignedUrlExpiration());

		generatePresignedUrlRequest.addRequestParameter(
			Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString()
		);
		return generatePresignedUrlRequest;
	}

	private Date getPresignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += EXPIRATION_TIME;
		expiration.setTime(expTimeMillis);

		return expiration;
	}

	@Transactional
	public void changeAttachFileUrl(String url, Board board) {
		board.changeAttachFileUrl(url);
		boardRepository.save(board);
	}

	@Transactional
	public void saveImageFileUrl(String url, Board board) {
		ImageFile imageFile = ImageFile.of(url, board);
		boardRepository.save(board);
		imageFileRepository.save(imageFile);
	}

	@Transactional
	public void deleteImageFileUrl(Board board, FileRequest fileRequest) {
		ImageFile imageFile = imageFileRepository.findByUrlAndBoard(fileRequest.getUrl(), board)
			.orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_IMAGE_FILE));
		imageFileRepository.delete(imageFile);
	}
}
