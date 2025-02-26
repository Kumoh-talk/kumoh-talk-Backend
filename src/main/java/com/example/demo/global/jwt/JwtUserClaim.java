package com.example.demo.global.jwt;


import com.example.demo.application.user.dto.vo.Role;
import com.example.demo.infra.user.entity.User;

public record  JwtUserClaim(
	Long userId,
	Role role
) {
	public static JwtUserClaim create(User user) {
		return new JwtUserClaim(user.getId(), user.getRole());
	}
}
