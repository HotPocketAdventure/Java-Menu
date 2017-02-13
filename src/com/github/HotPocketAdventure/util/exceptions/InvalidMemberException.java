package com.github.HotPocketAdventure.util.exceptions;

/**
 * An exception to log any error caused by degenerate Menus.
 * <br>Reference the {@link com.github.HotPocketAdventure.util.Menu Menu}'s rules {@link com.github.HotPocketAdventure.util.Menu#validateMembers() here}.
 * @author Michael Bradley
 *
 */
public class InvalidMemberException extends RuntimeException {
	
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
