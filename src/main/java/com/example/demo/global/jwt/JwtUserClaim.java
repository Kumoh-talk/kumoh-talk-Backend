package com.example.demo.global.jwt;


import com.example.demo.domain.user.vo.Role;
import com.example.demo.infra.user.entity.User;

public record  JwtUserClaim(
		Long userId,
		String nickname,
		Role role
) {
	public static JwtUserClaim create(User user) {
		return new JwtUserClaim(user.getId(), user.getNickname(), user.getRole());
	}

	public static JwtUserClaim create(Long userId, String nickname, Role role) {
		return new JwtUserClaim(userId, nickname, role);
	}
}
