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
		Class<?> declaringClass = method.getDeclaringClass();

		String basePath = extractClassLevelPath(declaringClass);
		String methodPath = extractMethodPath(method).orElse("");
		String restMethod = extractHttpMethod(method).orElse(null);

		// 최종 Endpoint 생성
		String endpoint = basePath + methodPath;

		// API 호출 시간 측정 시작
		Timer.Sample sample = apiTimer.startMetricApi();
		apiTimer.startTimer();
		log.info("API 호출 url: {}, http_method : {}", endpoint, restMethod);

		Object result;
		try {
			result = joinPoint.proceed();
		} catch (Exception e) {
			MDC.clear();
			apiTimer.clean();
			throw e;
		}

		// API 호출 시간 측정 종료
		apiTimer.endMetricApi(sample, endpoint, Optional.ofNullable(restMethod));
		Long elapsedTime = apiTimer.endTimer();

		log.info("API 호출 완료({}ms) url: {}, http_method : {}", elapsedTime, endpoint, restMethod);
		return result;
	}

	/**
	 * 클래스 레벨의 @RequestMapping(value)를 안전하게 추출.
	 * 예: @RequestMapping("/api") -> "/api"
	 */
	private String extractClassLevelPath(Class<?> declaringClass) {
		if (declaringClass.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping requestMapping = declaringClass.getAnnotation(RequestMapping.class);
			String[] values = requestMapping.value();
			if (values.length > 0) {
				return values[0];
			}
		}
		return "";
	}

	/**
	 * 메서드 레벨의 경로(@GetMapping, @PostMapping 등)를 하나씩 확인하여 추출.
	 * 예: @GetMapping("/hello") -> "/hello"
	 */
	private Optional<String> extractMethodPath(Method method) {
		if (method.isAnnotationPresent(GetMapping.class)) {
			return getValueSafely(method.getAnnotation(GetMapping.class).value());
		} else if (method.isAnnotationPresent(PostMapping.class)) {
			return getValueSafely(method.getAnnotation(PostMapping.class).value());
		} else if (method.isAnnotationPresent(PutMapping.class)) {
			return getValueSafely(method.getAnnotation(PutMapping.class).value());
		} else if (method.isAnnotationPresent(DeleteMapping.class)) {
			return getValueSafely(method.getAnnotation(DeleteMapping.class).value());
		} else if (method.isAnnotationPresent(PatchMapping.class)) {
			return getValueSafely(method.getAnnotation(PatchMapping.class).value());
		} else if (method.isAnnotationPresent(RequestMapping.class)) {
			// @RequestMapping(method=..., value=...) 형태
			return getValueSafely(method.getAnnotation(RequestMapping.class).value());
		}
		return Optional.empty();
	}

	/**
	 * 메서드 레벨의 HTTP 메서드를 추출.
	 * 예: @GetMapping -> "GET", @RequestMapping(method = RequestMethod.GET) -> "GET"
	 */
	private Optional<String> extractHttpMethod(Method method) {
		if (method.isAnnotationPresent(GetMapping.class)) {
			return Optional.of("GET");
		} else if (method.isAnnotationPresent(PostMapping.class)) {
			return Optional.of("POST");
		} else if (method.isAnnotationPresent(PutMapping.class)) {
			return Optional.of("PUT");
		} else if (method.isAnnotationPresent(DeleteMapping.class)) {
			return Optional.of("DELETE");
		} else if (method.isAnnotationPresent(PatchMapping.class)) {
			return Optional.of("PATCH");
		} else if (method.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
			if (requestMapping.method().length > 0) {
				// 첫 번째 RequestMethod로 설정
				return Optional.of(requestMapping.method()[0].name());
			}
		}
		return Optional.empty();
	}

	/**
	 * 매핑 어노테이션의 value 배열에서 첫 번째 요소를 안전하게 추출.
	 */
	private Optional<String> getValueSafely(String[] values) {
		if (values != null && values.length > 0) {
			return Optional.of(values[0]);
		}
		return Optional.empty();
	}
}
