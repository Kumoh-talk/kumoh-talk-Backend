package com.example.demo.base.config;



import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@TestConfiguration
public class RedisContainerConfig {

	private static final String REDIS_DOCKER_IMAGE = "redis:5.0.3-alpine";
	private static final Integer REDIS_PORT = 6379;

	static {    // (1)
		GenericContainer<?> REDIS_CONTAINER =
			new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
				.withExposedPorts(REDIS_PORT)
				.withReuse(true);

		REDIS_CONTAINER.start();    // (2)

		// (3)
		System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
		System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(REDIS_PORT).toString());
	}


}
