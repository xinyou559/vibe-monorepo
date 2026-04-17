package com.example.apiserver.common;

public enum ErrorCode {
	OK(0, "OK"),
	PARAM_INVALID(10001, "参数不合法"),
	INTERNAL_ERROR(20000, "系统异常");

	private final int code;
	private final String message;

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
