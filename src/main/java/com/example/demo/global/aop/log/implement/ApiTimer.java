package com.example.demo.global.aop.log.implement;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiTimer {
	private ThreadLocal<Long> startTime = new ThreadLocal<>();

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
