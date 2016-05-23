package org.bimserver.serviceplatform;

public class UserException extends Exception {

	private static final long serialVersionUID = -6191278977728656460L;
	private int code;

	public UserException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	public UserException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}

	public UserException(ErrorCode errorCode, String message) {
		super(errorCode.getMessage() + ": " + message);
	}

	public int getCode() {
		return code;
	}
}