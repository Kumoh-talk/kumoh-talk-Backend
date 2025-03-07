package com.example.demo.domain.user.entity;

import com.example.demo.domain.user.vo.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserTarget {
	private final Long userId;
	private final String nickName;
	private final Role userRole;

	@Builder
	private UserTarget(Long userId, String nickName, Role userRole) {
		this.userId = userId;
		this.nickName = nickName;
		this.userRole = userRole;
	}
}
