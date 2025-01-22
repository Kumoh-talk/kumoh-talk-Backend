package com.example.demo.domain.board.service.service.view;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.global.event.view.BoardViewEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewBulkUpdateService implements ViewIncreaseService {
	private final RedisTemplate<String,Object> redisTemplate;
	private final ApplicationEventPublisher applicationEventPublisher;
	private static final String BOARD_VIEW_KEY_PREFIX = "board:view:";

	@Override
	public void increaseView(Long boardId) {
		String boardViewKey = BOARD_VIEW_KEY_PREFIX + boardId;
		redisTemplate.opsForValue().increment(boardViewKey, 1);
		applicationEventPublisher.publishEvent(new BoardViewEvent(this,boardId));
	}
}
