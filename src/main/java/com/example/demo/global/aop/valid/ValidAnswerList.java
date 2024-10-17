package com.example.demo.global.aop.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AnswerListValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAnswerList {
    String message() default "유효하지 않은 답변 리스트입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
