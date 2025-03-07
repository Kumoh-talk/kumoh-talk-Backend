package com.example.demo.global.jwt;

import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.user.service.UserAdminService;
import com.example.demo.domain.user.vo.Role;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.service.UserAdditionalInfoService;
import com.example.demo.global.jwt.exception.AdditionalInfoNotUpdatedException;
import com.example.demo.global.jwt.exception.JwtAccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

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
	private final UserAdminService userAdminService;
	private final UserAdditionalInfoService userAdditionalInfoService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		String tokenValue = jwtAuthenticationToken.token();
		if (tokenValue == null) {
			return null;
		}

		try {
			JwtUserClaim claims = jwtHandler.parseToken(tokenValue);
			this.validateAdminRole(claims);
			this.validateUserAdditionalInfo(claims);
			return new JwtAuthentication(claims);
		} catch (ExpiredJwtException e) {
			throw new JwtTokenExpiredException(e);
		} catch (JwtAccessDeniedException | AdditionalInfoNotUpdatedException e) {
			throw e;
		} catch (Exception e) {
			throw new JwtTokenInvalidException(e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

	private void validateAdminRole(JwtUserClaim claims) {
		if (Role.ROLE_ADMIN.equals(claims.role()) && !userAdminService.isAdmin(claims.userId())) {
			throw new JwtAccessDeniedException();
		}
	}

	private void validateUserAdditionalInfo(JwtUserClaim claims) {
		// ROLE_USER 이상의 권한 확인
		if (Role.ROLE_ACTIVE_USER.equals(claims.role()) || Role.ROLE_SEMINAR_WRITER.equals(claims.role()) || Role.ROLE_ADMIN.equals(claims.role())) {
			UserAdditionalInfoData additionalInfoData = userAdditionalInfoService.getUserAdditionalInfoData(claims.userId());
			if (additionalInfoData != null && !additionalInfoData.isUpdated()) {
				throw new AdditionalInfoNotUpdatedException();
			}
		}
	}
}
