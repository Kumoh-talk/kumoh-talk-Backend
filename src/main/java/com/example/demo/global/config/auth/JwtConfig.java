package com.example.demo.global.config.auth;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtProperties;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

	@Bean
	public JwtHandler jwtHandler(JwtProperties jwtProperties) {
		return new JwtHandler(jwtProperties);
	}
}
