package com.d2ticom.api.common.exception;

public class ObjectnotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ObjectnotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectnotFoundException(String message) {
		super(message);
	}

}
