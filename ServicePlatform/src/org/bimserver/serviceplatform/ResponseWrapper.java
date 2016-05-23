package org.bimserver.serviceplatform;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ResponseWrapper {

	public abstract void forward(String url) throws IOException;
	public abstract void setHeader(String key, String value);
	public abstract OutputStream getOutputStream() throws IOException;
}
