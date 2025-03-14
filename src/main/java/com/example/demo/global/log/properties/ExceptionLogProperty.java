package com.example.demo.global.log.properties;

import java.util.Arrays;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.MethodSignature;

import org.aspectj.lang.reflect.MethodSignature;

/*
 * @deprecated
 * 이 클래스는 더 이상 사용되지 않습니다.
 */
@Deprecated
public class ExceptionLogProperty extends LogProperty {

	private ExceptionLogProperty(String description, String className, String methodName,
		ServiceException serviceException) {
		super();
		super.description = description;
		super.className = className;
		super.methodName = methodName;
		ErrorCode errorCode = serviceException.getErrorCode();
		Map<String, String> map = Map.of(
			"httpStatus", errorCode.toString(),
			"errorCode", String.valueOf(errorCode.getCode()),
			"message", errorCode.getMessage()
		);
		super.args = new Map[] {map};
	}

	public static ExceptionLogProperty of(String description, JoinPoint joinPoint, ServiceException serviceException) {
		return new ExceptionLogProperty(
			description,
			joinPoint.getSignature().getDeclaringTypeName(),
			(joinPoint.getSignature()).getName(),
			serviceException
		);
	}

	@Override
	public String toString() {
		return "{"
			+ "description='" + description + '\''
			+ ", className='" + className + '\''
			+ ", methodName='" + methodName + '\''
			+ ", args=" + Arrays.toString(args)
			+ '}';
	}
}
