package com.example.demo.global.regex;

public interface S3UrlRegex {
	// S3 버킷 URL 정규식 (https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/board/{boardId}/{image|attach}/uuid/{fileName})
	String S3_BOARD_FILE_URL = "https:\\/\\/kumoh-talk-bucket\\.s3\\.ap-northeast-2\\.amazonaws\\.com\\/board\\/\\d+\\/(image|attach)\\/.*\\/[a-zA-Z0-9\\.\\-]+";

	String S3_PROFILE_FILE_URL = "https:\\/\\/kumoh-talk-bucket\\.s3\\.ap-northeast-2\\.amazonaws\\.com\\/profile\\/\\d+\\/image\\/.*\\/[a-zA-Z0-9\\.\\-\\_]+";
}
