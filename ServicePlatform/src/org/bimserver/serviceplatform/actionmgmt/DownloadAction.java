package org.bimserver.serviceplatform.actionmgmt;

import org.bimserver.serviceplatform.DataSource;
import org.bimserver.serviceplatform.HttpRequest;
import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.RequestParameters;

public abstract class DownloadAction extends Action {

	public abstract DataSource execute(RequestParameters requestParameters) throws Exception;

	public DataSource process(HttpRequest request) throws Exception {
		DataSource dataSource = execute(new RequestParameters(request, this.getClass().getAnnotation(RequestMapping.class)));
		return dataSource;
	}

	public DataSource processWithRetries(HttpRequest request) throws Exception {
		return process(request);
	}
}