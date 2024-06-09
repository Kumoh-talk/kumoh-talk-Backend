package com.example.demo.global.jwt;

import static com.example.demo.global.base.exception.ErrorCode.*;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.exception.JwtAuthentication;
import com.example.demo.global.jwt.exception.JwtTokenExpiredException;
import com.example.demo.global.jwt.exception.JwtTokenInvalidException;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider implements AuthenticationProvider {

	private final JwtHandler jwtHandler;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String tokenValue = (String)token.getPrincipal();
		if (tokenValue == null) {
			return null;
		}

		try{
			JwtUserClaim claims = jwtHandler.parseToken(tokenValue);
			return new JwtAuthentication(claims);
		} catch (ExpiredJwtException e) {
			throw new JwtTokenExpiredException(e);
		} catch (Exception e) {
			throw new JwtTokenInvalidException(e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
