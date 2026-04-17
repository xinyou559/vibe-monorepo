package com.example.apiserver.common;

public class Result<T> {

	private final int code;
	private final String message;
	private final T data;

	private Result(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static <T> Result<T> ok(T data) {
		return new Result<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), data);
	}

	public static <T> Result<T> fail(ErrorCode errorCode) {
		return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
	}

	public static <T> Result<T> fail(ErrorCode errorCode, String message) {
		return new Result<>(errorCode.getCode(), message, null);
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}
}
