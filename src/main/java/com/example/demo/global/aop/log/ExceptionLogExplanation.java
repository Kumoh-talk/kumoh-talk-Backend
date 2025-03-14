package com.example.demo.global.aop.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.log.properties.LogLevel;

/**
 * 해당 클래스는 곧 없어질 예정입니다.
 * @deprecated
 * 일시: 2025-03-14
 * 이슈번호: #90
 */
@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionLogExplanation {
	String description() default "";

	ErrorCode code();

	LogLevel level() default LogLevel.WARN;
}
