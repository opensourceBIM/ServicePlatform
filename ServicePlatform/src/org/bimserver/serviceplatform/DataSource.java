package org.bimserver.serviceplatform;

import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;

public interface DataSource {

	public InputStream getInputStream() throws IOException;
	public void close() throws IOException;
	public long size() throws IOException;
	public String getContentType();
	public GregorianCalendar getDate();
	public String getName();
}