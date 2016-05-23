package org.bimserver.serviceplatform;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 7582291260216090271L;

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Exception e) {
		super(e);
	}
}
