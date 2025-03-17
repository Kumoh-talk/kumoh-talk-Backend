package com.example.demo.global.aop.log.aspect;

import java.lang.reflect.Method;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.global.aop.log.implement.ApiTimer;

import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiMonitorAspect {

	private final ApiTimer apiTimer;

	@Around("within(@org.springframework.web.bind.annotation.RestController *)")
	public Object monitorApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();

		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = method.getName();

		// API 호출 시간 측정 시작
		apiTimer.startTimer();
		log.info("API 호출 class: {}, method : {}", className, methodName);

		Object result;
		try {
			result = joinPoint.proceed();
		} catch (Exception e) {
			MDC.clear();
			apiTimer.clean();
			throw e;
		}

		// API 호출 시간 측정 종료
		Long elapsedTime = apiTimer.endTimer();

		log.info("API 호출 완료({}ms) class: {}, method : {}", elapsedTime, className, methodName);
		return result;
	}
}
