package org.bimserver.serviceplatform;

public class MissingArgumentException extends Exception {

	private static final long serialVersionUID = 663133441996619229L;

	public MissingArgumentException(String message) {
		super(message);
	}
}
