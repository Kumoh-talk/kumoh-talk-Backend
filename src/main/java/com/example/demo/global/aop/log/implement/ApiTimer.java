package com.example.demo.global.aop.log.implement;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiTimer {
	private final MeterRegistry meterRegistry;
	private ThreadLocal<Long> startTime = new ThreadLocal<>();

	public Timer.Sample startMetricApi() {
		return Timer.start(meterRegistry);
	}

	public void endMetricApi(Timer.Sample sample,String endpoint, Optional<String> restMethod) {
		sample.stop(Timer.builder("api.request.latency")
			.description("API 요청 평균 지연 시간(ms)")
			.tag("endpoint", endpoint)
			.tag("method", restMethod.orElse("null"))
			.register(meterRegistry));
	}

	public void startTimer() {
		startTime.set(System.currentTimeMillis());
	}

	public Long endTimer() {
		return System.currentTimeMillis() - startTime.get();
	}

	public void clean() {
		startTime.remove();
	}
}
