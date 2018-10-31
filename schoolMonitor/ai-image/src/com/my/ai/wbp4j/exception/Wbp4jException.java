package com.my.ai.wbp4j.exception;

public class Wbp4jException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Wbp4jException() {
    }

    public Wbp4jException(String message) {
        super(message);
    }
}
