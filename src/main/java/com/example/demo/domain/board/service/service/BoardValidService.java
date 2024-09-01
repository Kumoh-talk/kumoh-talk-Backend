package com.example.demo.domain.board.service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardValidService {
	private final UserRepository userRepository;

	/*
	 * 세미나 게시판에 글을 작성할 수 있는 권한이 있는지 확인하는 메소드 (추후 추가 예정)
	 */
	@Transactional(readOnly = true)
	public void validateSeminarRole(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		if(user.getRole().equals("ROLE_USER")) { // TODO : 현재는 ROLE_USER가 세미나 신청을 한적이 있는지로 판단하고 있음 권한 세분화하면 변경 필요
			throw new ServiceException(ErrorCode.NOT_SEMINAR_ROLE);
		}
	}

	/**
	 * 공지사항 게시판에 글을 작성할 수 있는 권한이 있는지 확인하는 메소드 (ADMIN만 가능)
	 */
	@Transactional(readOnly = true)
	public void validateNoticeRole(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		if(user.getRole().equals("ROLE_ADMIN")) {
			throw new ServiceException(ErrorCode.NOT_NOTICE_ROLE);
		}
	}
}
