package com.example.demo.global.oauth.user.naver;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.global.oauth.user.OAuth2Properties;
import com.example.demo.global.oauth.user.OAuth2UserUnlink;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EnableConfigurationProperties(OAuth2Properties.class)
@RequiredArgsConstructor
@Component
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink {

	private static final String URL = "https://nid.naver.com/oauth2.0/token";
	private final RestTemplate restTemplate;
	private final OAuth2Properties oAuth2Properties;

	@Override
	public void unlink(String accessToken) {
		String url = URL +
			"?service_provider=NAVER" +
			"&grant_type=delete" +
			"&client_id=" +
			oAuth2Properties.getNaver().getClientId() +
			"&client_secret=" +
			oAuth2Properties.getNaver().getClientSecret() +
			"&access_token=" +
			accessToken;

		UnlinkResponse response = restTemplate.getForObject(url, UnlinkResponse.class);

		if (response != null && !"success".equalsIgnoreCase(response.getResult())) {
			throw new RuntimeException("Failed to Naver Unlink");
		}
	}

	@Getter
	@RequiredArgsConstructor
	public static class UnlinkResponse {
		@JsonProperty("access_token")
		private final String accessToken;
		private final String result;
	}
}
