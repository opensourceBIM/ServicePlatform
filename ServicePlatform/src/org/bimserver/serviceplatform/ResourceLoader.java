package org.bimserver.serviceplatform;

import java.io.InputStream;

public interface ResourceLoader {
	InputStream getResource(String name);
}
