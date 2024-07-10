package com.example.demo.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.global.jwt.JwtAuthentication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class UserIdInjectionAspect {
	@Pointcut("@annotation(com.example.demo.global.aop.AssignUserId) && args(userId, ..)")
	public void userIdAnnotatedMethod(Long userId) {}

	@Around("userIdAnnotatedMethod(userId)")
	public Object injectUserId(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication instanceof JwtAuthentication) {
			userId = (Long) authentication.getPrincipal();
		} else {
			log.error("JwtAuthentication Type Casting Error {}", authentication.getClass());
			throw new IllegalArgumentException("Only JWT-based authentication is supported for @AssignUserId annotation.");
		}

		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof Long) {
				args[i] = userId;
				break;
			}
		}

		return joinPoint.proceed(args);
	}
}
