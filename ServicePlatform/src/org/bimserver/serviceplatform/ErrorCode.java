package org.bimserver.serviceplatform;

public enum ErrorCode {
	UNKNOWN_ERROR(1, "Unknown"), 
	TOO_MANY_DEADLOCKS(2, "Too many deadlocks"), 
	INVALID_JSON(3, "Invalid JSON"), 
	INVALID_FIELD_VALUE(4, "Invalid field value"), 
	INSUFFICIENT_RIGHTS(5, "Insufficient rights"), 
	ACCESS_TOKEN_REQUIRED(6, "Access token required"), 
	DATABASE_ERROR(7, "Database error"), 
	INVALID_ACCESS_TOKEN(8, "Invalid access token"), 
	INVALID_LOGIN(9, "Invalid login");

	private int code;
	private String message;

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
}