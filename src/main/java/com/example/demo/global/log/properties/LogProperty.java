package com.example.demo.global.log.properties;

/*
* @deprecated
* 이 클래스는 더 이상 사용되지 않습니다.
 */
@Deprecated
public abstract class LogProperty {
	String description;
	String className;
	String methodName;
	Object[] args;

	public LogProperty() {
	}

	public LogProperty(String description, String className, String methodName, Object[] args) {
		this.description = description;
		this.className = className;
		this.methodName = methodName;
		this.args = args;
	}
}
