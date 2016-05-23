package org.bimserver.serviceplatform.actionmgmt;

import java.net.URI;

public class RedirectException extends Exception {

	private static final long serialVersionUID = -3567573253961385278L;
	private URI uri;

	public RedirectException(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}
}
