package com.example.demo.global.config.auth;

import com.example.demo.domain.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtProperties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

	private final RefreshTokenRepository refreshTokenRepository;

	@Bean
	public JwtHandler jwtHandler(JwtProperties jwtProperties) {
		return new JwtHandler(jwtProperties, refreshTokenRepository);
	}
}
