package com.github.HotPocketAdventure.util.exceptions;

public class InvalidMemberException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 0xDEAD;

	public InvalidMemberException() {
		super();
	}
	
	public InvalidMemberException(String message) {
		super(message);
	}
	
	public InvalidMemberException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidMemberException(String message, Throwable cause, boolean enableSuppression, boolean writeStackTrace) {
		super(message, cause, enableSuppression, writeStackTrace);
	}
	
	public InvalidMemberException(Throwable cause) {
		super(cause);
	}
}
