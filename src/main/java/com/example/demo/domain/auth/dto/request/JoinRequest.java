package com.example.demo.domain.auth.dto.request;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.regex.UserRegex;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
	@Pattern(regexp = UserRegex.USERID_REGEXP, message = "아이디 형식에 맞지 않습니다.")
	private String userId;

	@Pattern(regexp = UserRegex.EMAIL_REGEXP, message = "이메일 형식이 맞지 않습니다.")
	private String email;

	@Pattern(regexp = UserRegex.NICKNAME_REGEXP, message = "닉네임 형식이 맞지 않습니다.")
	private String nickname;

	@Pattern(regexp = UserRegex.PASSWORD_REGEXP, message = "비밀번호 형식이 맞지 않습니다.")
	private String password;

	private String name;

	private String department;

	private String field;

	public User toEntity(String hashedPassword) {
		return User.builder()
			.userId(this.userId)
			.email(this.email)
			.nickname(this.nickname)
			.password(hashedPassword)
			.role(Role.ROLE_USER)
			.name(this.name)
			.department(this.department)
			.field(this.field)
			.build();
	}
}
