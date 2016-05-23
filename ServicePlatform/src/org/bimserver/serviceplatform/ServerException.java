package org.bimserver.serviceplatform;

public class ServerException extends Exception {

	private static final long serialVersionUID = 2971497277730900238L;
	private int code;

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(String message) {
		super(message);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}

	public ServerException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public ServerException(ErrorCode error) {
		this(error.getMessage(), error.getCode());
	}
}