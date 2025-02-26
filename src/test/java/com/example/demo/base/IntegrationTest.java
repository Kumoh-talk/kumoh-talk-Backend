package com.example.demo.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.base.config.RedisContainerConfig;
import com.example.demo.builder.BuilderSupporter;
import com.example.demo.builder.TestFixtureBuilder;
import com.example.demo.domain.board.service.usecase.BoardAdminService;
import com.example.demo.domain.board.service.usecase.BoardService;
import com.example.demo.infra.builder.JpaBuilderSupporter;
import com.example.demo.infra.builder.JpaTestFixtureBuilder;

@SpringJUnitConfig // Spring 테스트 컨텍스트 활성화
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Import(value = {TestFixtureBuilder.class, BuilderSupporter.class, RedisContainerConfig.class, JpaTestFixtureBuilder.class,
	JpaBuilderSupporter.class})
@RecordApplicationEvents // 애플리케이션 이벤트 기록 활성화
public abstract class IntegrationTest {


	@Autowired
	protected TestFixtureBuilder testFixtureBuilder;

	@Autowired
	protected RedisTemplate<String, Object> redisTemplate;

	@Autowired
	protected ApplicationEvents events;

	@Autowired
	protected BoardService boardService;

	@Autowired
	protected JpaTestFixtureBuilder jpaTestFixtureBuilder;

	@Autowired
	protected BoardAdminService boardAdminService;

}
