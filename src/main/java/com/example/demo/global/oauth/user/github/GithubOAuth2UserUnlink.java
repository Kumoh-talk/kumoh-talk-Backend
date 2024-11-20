package com.example.demo.global.oauth.user.github;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONObject;

import com.example.demo.global.oauth.user.OAuth2Properties;
import com.example.demo.global.oauth.user.OAuth2UserUnlink;

import lombok.RequiredArgsConstructor;

@EnableConfigurationProperties(OAuth2Properties.class)
@RequiredArgsConstructor
@Component
public class GithubOAuth2UserUnlink implements OAuth2UserUnlink {

	private static final String UNLINK_URL_HEAD = "https://api.github.com/applications/";
	private static final String UNLINK_URL_TAIL = "/grant";
	public static final String ACCESS_TOKEN = "access_token";
	private final RestTemplate restTemplate;
	private final OAuth2Properties oAuth2Properties;

	@Override
	public void unlink(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(oAuth2Properties.getGithub().getClientId(), oAuth2Properties.getGithub().getClientSecret());
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject requestBody = new JSONObject();
		requestBody.put(ACCESS_TOKEN, accessToken);

		HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
		restTemplate.exchange(
			UNLINK_URL_HEAD + oAuth2Properties.getGithub().getClientId() + UNLINK_URL_TAIL,
			HttpMethod.DELETE,
			entity,
			Object.class);
	}
}
