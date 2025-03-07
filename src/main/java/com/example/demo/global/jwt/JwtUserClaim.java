package com.example.demo.global.jwt;


import com.example.demo.domain.user.vo.Role;
import com.example.demo.infra.user.entity.User;

public record  JwtUserClaim(
	Long userId,
	Role role
) {
	public static JwtUserClaim create(User user) {
		return new JwtUserClaim(user.getId(), user.getRole());
	}
	public static JwtUserClaim create(Long userId, Role role) {
		return new JwtUserClaim(userId, role);
	}
}
