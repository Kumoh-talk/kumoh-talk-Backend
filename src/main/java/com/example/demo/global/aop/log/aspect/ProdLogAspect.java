package com.example.demo.global.aop.log.aspect;

import java.lang.reflect.Method;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.global.aop.log.ExceptionLogExplanation;
import com.example.demo.global.aop.log.GeneralLogExplanation;
import com.example.demo.global.aop.log.LogExplanations;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.log.properties.ExceptionLogProperty;
import com.example.demo.global.log.properties.InfoLogProperty;
import com.example.demo.global.log.properties.LogLevel;
import com.example.demo.global.log.properties.LogProperty;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 해당 클래스는 곧 없어질 예정입니다. ApiMonitorAspect를 사용해주세요.
 * @deprecated
 * @see com.example.demo.global.aop.log.aspect.ApiMonitorAspect
 * 일시: 2025-03-14
 * 이슈번호: #90
 */
@Deprecated
@Aspect
@Component
@Profile("disable")
@Slf4j
@RequiredArgsConstructor
public class ProdLogAspect {
	private final MeterRegistry meterRegistry;
	private ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Around("com.example.demo.global.aop.log.pointcut.LogPointCut.controllerInfoLoggingPointcut()")
	public Object controllerInfoLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		LogProperty logProperty = InfoLogProperty.of(
			getLogDescription(methodSignature),
			joinPoint,
			methodSignature.getName()
		);
		Method method = methodSignature.getMethod();


		startTime.set(System.currentTimeMillis());
		Timer.Sample sample = Timer.start(meterRegistry);

		if (method.isAnnotationPresent(RequestMapping.class)) {
			String endpoint = method.getAnnotation(RequestMapping.class).value()[0];
			MDC.put("endpoint", endpoint);
		}
		log.info("호출 {}", logProperty);

		Object result;
		try {
			result = joinPoint.proceed();
		} catch (Exception e) {
			MDC.clear();
			startTime.remove();
			throw e;
		}

		logProperty = InfoLogProperty.of(
			getLogDescription(methodSignature),
			joinPoint,
			Optional.ofNullable(result)
		);

		// 지연시간 측정
		sample.stop(Timer.builder("api.request.latency")
			.description("API 요청 평균 지연 시간(ms)")
			.tag("class", joinPoint.getTarget().getClass().getSimpleName())
			.tag("method", joinPoint.getSignature().getName())
			.register(meterRegistry));

		long elapsedTime = System.currentTimeMillis() - startTime.get();
		log.info("성공 (수행 시간: {} ms) {}", elapsedTime, logProperty);

		MDC.clear();
		startTime.remove();

		return result;
	}

	@AfterThrowing(pointcut = "com.example.demo.global.aop.log.pointcut.LogPointCut.customExceptionLoggingPointcut()",
		throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
		if (MDC.get("isCustomThrow") != null) {
			return;
		}

		LogProperty logProperty;
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		if (exception instanceof ServiceException serviceException) {
			String description = getLogExplanationDescription(serviceException, signature);

			logProperty = ExceptionLogProperty.of(description, joinPoint, serviceException);
			startTime.remove();

			LogLevel logExplanationLogLevel = getLogExplanationLogLevel(serviceException, signature);
			if (logExplanationLogLevel == LogLevel.ERROR) {
				log.error("실패 {}", logProperty);
			} else {
				log.warn("실패 {}", logProperty);
			}
			MDC.put("isCustomThrow", "true");
		}
	}

	private LogLevel getLogExplanationLogLevel(ServiceException serviceException, MethodSignature signature) {
		LogLevel logLevel = LogLevel.WARN;
		if (signature.getMethod().isAnnotationPresent(LogExplanations.class)) {
			ExceptionLogExplanation[] exceptionLogExplanations = signature.getMethod()
				.getAnnotation(LogExplanations.class)
				.exception();
			for (ExceptionLogExplanation exceptionLogExplanation : exceptionLogExplanations) {
				if (exceptionLogExplanation.code().getCode().equals(serviceException.getErrorCode().getCode())) {
					logLevel = exceptionLogExplanation.level();
				}
			}
		}
		return logLevel;
	}

	private String getLogExplanationDescription(ServiceException serviceException, MethodSignature signature) {
		String description = "";
		if (signature.getMethod().isAnnotationPresent(LogExplanations.class)) {
			ExceptionLogExplanation[] exceptionLogExplanations = signature.getMethod()
				.getAnnotation(LogExplanations.class)
				.exception();
			for (ExceptionLogExplanation exceptionLogExplanation : exceptionLogExplanations) {
				if (exceptionLogExplanation.code() == serviceException.getErrorCode()) {
					description = exceptionLogExplanation.description();
				}
			}
		}
		return description;
	}

	private String getLogDescription(MethodSignature methodSignature) {
		String description = "";
		if (methodSignature.getMethod().isAnnotationPresent(LogExplanations.class)) {
			GeneralLogExplanation[] general = methodSignature.getMethod()
				.getAnnotation(LogExplanations.class)
				.general();
			if (general.length > 0) {
				description = general[0].description();
			}
		}
		return description;
	}
}
