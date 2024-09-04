package com.example.demo.global.utils;

import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3PresignedUrlUtil {

	private static final Integer EXPIRATION_TIME = 1000 * 60 * 2;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3Client amazonS3Client;

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


}
