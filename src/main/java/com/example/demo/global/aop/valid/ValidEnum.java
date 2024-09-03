package com.example.demo.global.aop.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
	String message() default "Invalid value. This is not permitted.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	Class<? extends java.lang.Enum<?>> enumClass();
}
