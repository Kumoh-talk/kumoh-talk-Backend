package com.example.demo.global.aop.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.log.properties.LogLevel;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionLogExplanation {
	String description() default "";

	ErrorCode code();

	LogLevel level() default LogLevel.WARN;
}
