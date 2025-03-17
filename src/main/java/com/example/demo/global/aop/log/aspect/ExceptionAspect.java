package com.example.demo.global.aop.log.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.log.properties.ExceptionLogProperty;
import com.example.demo.global.log.properties.LogLevel;
import com.example.demo.global.log.properties.LogProperty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionAspect {
	@AfterThrowing(pointcut = "within(@org.springframework.stereotype.Component *) && !within(@org.springframework.web.bind.annotation.RestController *)",
		throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
		if (MDC.get("isCustomThrow") != null) {
			return;
		}

		MethodSignature signature = (MethodSignature)joinPoint.getSignature();

		if (exception instanceof ServiceException serviceException) {
			ErrorCode errorCode = serviceException.getErrorCode();
			log.warn("도메인 에러 {클래스: {}, 메서드{}, 에러코드: {}, 에러메시지: {}}",
				signature.getDeclaringTypeName(),
				signature.getName(),
				errorCode.getCode(),
				errorCode.getMessage());
		}else{
			log.error("예외 발생 {클래스: {}, 메서드{}, 에러메시지: {}}",
				signature.getDeclaringTypeName(),
				signature.getName(),
				exception.getMessage());
		}

		MDC.put("isCustomThrow", "true");
	}
}
