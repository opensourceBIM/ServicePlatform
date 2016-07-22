package org.bimserver.serviceplatform;

import org.bimserver.serviceplatform.actionmgmt.ActionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Context {
	private ObjectMapper objectMapper = new ObjectMapper();
	private String forwardUrl;
	private HttpServletRequestWrapper httpRequest;
	private ResponseWrapper responseWrapper;
	private String accessToken;
	private TokenManager tokenManager;
	private ServerContext serverContext;

	public Context(ServerContext serverContext, HttpServletRequestWrapper httpRequest, ResponseWrapper responseWrapper) {
		this.serverContext = serverContext;
		this.httpRequest = httpRequest;
		this.responseWrapper = responseWrapper;
		this.accessToken = httpRequest.getParameter("access_token");
	}

	public boolean isForwarded() {
		return forwardUrl != null;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public ActionFactory getActionFactory() {
		return serverContext.getActionFactory();
	}

	public HttpServletRequestWrapper getHttpServletRequest() {
		return httpRequest;
	}

	public void forward(String url) {
		forwardUrl = url;
	}

	public TokenManager getTokenManager() {
		return tokenManager;
	}
}