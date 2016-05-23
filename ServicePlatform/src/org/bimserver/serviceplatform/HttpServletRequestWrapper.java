package org.bimserver.serviceplatform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class HttpServletRequestWrapper extends HttpRequest {

	private HttpServletRequest request;

	public HttpServletRequestWrapper(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getParameter(String key) {
		return request.getParameter(key);
	}

	@Override
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	@Override
	public String getHeader(String key) {
		return request.getHeader(key);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	@Override
	public Part getPart(String key) throws IOException, ServletException {
		return request.getPart(key);
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return request.getParts();
	}

	@Override
	public String getRequestURI() {
		return request.getRequestURI();
	}
	
	@Override
	public String toString() {
		return getRemoteAddr() + " " + request.getQueryString();
	}

	@Override
	public String getContentType() {
		return request.getContentType();
	}

	@Override
	public HttpSession getSession() {
		return request.getSession();
	}

	public HttpServletRequest getHttpServletRequest() {
		return request;
	}
}