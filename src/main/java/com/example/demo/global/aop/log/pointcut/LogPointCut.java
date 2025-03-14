package com.example.demo.global.aop.log.pointcut;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 해당 클래스는 곧 없어질 예정입니다.
 * @deprecated
 * 일시: 2025-03-14
 * 이슈번호: #90
 */
@Deprecated
public class LogPointCut {
	@Pointcut("execution(* com.example.demo.domain..*.*(..))")
	public void domainLoggingPointcut() {
	}

	@Pointcut("execution(* com.example.demo.global..*.*(..)) && within(@org.springframework.stereotype.Component *)")
	public void globalLoggingPointcut() {
	}

	@Pointcut("domainLoggingPointcut() &&  within(@org.springframework.web.bind.annotation.RestController *)")
	public void controllerInfoLoggingPointcut() {
	}

	@Pointcut("domainLoggingPointcut() || globalLoggingPointcut()")
	public void customExceptionLoggingPointcut() {
	}

	@Pointcut("(domainLoggingPointcut() || globalLoggingPointcut()) && "
		+ "!within(@org.springframework.web.bind.annotation.RestController *)")
	public void debugLogPointcut() {
	}
}
