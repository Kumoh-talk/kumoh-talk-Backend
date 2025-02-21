package com.example.demo.global.jwt;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;

public record  JwtUserClaim(
	Long userId,
	Role role
) {
	public static JwtUserClaim create(User user) {
		return new JwtUserClaim(user.getId(), user.getRole());
	}
}
