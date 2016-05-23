package org.bimserver.serviceplatform;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class HttpResponseWrapper extends ResponseWrapper {
	private HttpServletResponse response;

	public HttpResponseWrapper(HttpServletResponse response) {
		this.response = response;
	}
	
	public void forward(String url) throws IOException {
		response.sendRedirect(url);
	}
	
	public OutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

	@Override
	public void setHeader(String key, String value) {
		response.setHeader(key, value);
	}
}
