package com.example.demo.global.config.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiSuccessResponseExplanation {
	HttpStatus status() default HttpStatus.OK;

	Class<?> responseClass() default EmptyClass.class;

	String description() default "";

	class EmptyClass {
	}
}

