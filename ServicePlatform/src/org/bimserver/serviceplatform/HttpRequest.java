package org.bimserver.serviceplatform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public abstract class HttpRequest {

	public abstract String getParameter(String string);

	public abstract String getRemoteAddr();

	public abstract String getHeader(String string);

	public abstract InputStream getInputStream() throws IOException;

	public abstract Part getPart(String string) throws IOException, ServletException;

	public abstract String getRequestURI();

	public abstract String getContentType();

	public abstract Collection<Part> getParts() throws IOException, ServletException;

	public abstract HttpSession getSession();
}