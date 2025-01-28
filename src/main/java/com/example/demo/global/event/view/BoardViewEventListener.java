package com.example.demo.global.event.view;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.domain.board.Repository.BoardRepository;

@Service
public class BoardViewEventListener {
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private final ConcurrentHashMap<Long, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();

	private final BoardRepository boardRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String BOARD_VIEW_KEY = "board:view:";

	@Autowired
	public BoardViewEventListener(BoardRepository boardRepository, RedisTemplate<String, Object> redisTemplate) {
		this.boardRepository = boardRepository;
		this.redisTemplate = redisTemplate;
	}

	@EventListener
	public void handleBoardViewEvent(BoardViewEvent event) {
		Long boardId = event.getBoardId();
		System.out.printf("이벤트 리스너 호출 - boardId: %d%n", boardId);

		// 이전에 예약된 작업이 있다면 취소
		if (taskMap.containsKey(boardId)) {
			ScheduledFuture<?> existingTask = taskMap.get(boardId);
			if (existingTask != null && !existingTask.isDone()) {
				return;
			}
		}

		// 새로운 작업 예약
		ScheduledFuture<?> newTask = scheduler.schedule(() -> {
			bulkUpdateView(event);
		}, 5, TimeUnit.MINUTES); // 5분 후 실행

		taskMap.put(boardId, newTask);
	}

	public void bulkUpdateView(BoardViewEvent event) {
		Long boardId = event.getBoardId();
		String boardViewKey = BOARD_VIEW_KEY + boardId;

		Object viewCountObj = redisTemplate.opsForValue().get(boardViewKey);
		int viewCount = (viewCountObj != null) ? Integer.parseInt(viewCountObj.toString()) : 0;
		// Redis 값 초기화
		redisTemplate.opsForValue().decrement(boardViewKey, viewCount);


		boardRepository.increaseViewCount(boardId, viewCount);

		// 작업 완료 후 해당 boardId의 예약 제거
		taskMap.remove(boardId);
	}
}
