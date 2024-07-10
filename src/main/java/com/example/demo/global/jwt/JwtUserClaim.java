package com.example.demo.global.jwt;

import com.example.demo.domain.user.domain.vo.Role;

public record JwtUserClaim(
	Long userId,
	Role role
) {
}
