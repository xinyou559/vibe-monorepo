package com.example.apiserver.common;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
	public Result<Void> handleValidationException(Exception ex) {
		String message = "参数不合法";
		if (ex instanceof MethodArgumentNotValidException e && e.getBindingResult().getFieldError() != null) {
			message = e.getBindingResult().getFieldError().getDefaultMessage();
		}
		if (ex instanceof BindException e && e.getBindingResult().getFieldError() != null) {
			message = e.getBindingResult().getFieldError().getDefaultMessage();
		}
		return Result.fail(ErrorCode.PARAM_INVALID, message);
	}

	@ExceptionHandler(Exception.class)
	public Result<Void> handleException(Exception ex) {
		return Result.fail(ErrorCode.INTERNAL_ERROR);
	}
}
