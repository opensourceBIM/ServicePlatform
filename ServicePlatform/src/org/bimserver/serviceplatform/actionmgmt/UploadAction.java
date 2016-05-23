package org.bimserver.serviceplatform.actionmgmt;

import java.util.Random;

import javax.servlet.http.Part;

import org.bimserver.serviceplatform.DeadlockLoserDataAccessException;
import org.bimserver.serviceplatform.ErrorCode;
import org.bimserver.serviceplatform.HttpRequest;
import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.RequestParameters;
import org.bimserver.serviceplatform.ServerException;
import org.bimserver.serviceplatform.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class UploadAction extends Action {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadAction.class);
	public abstract JsonNode execute(Part part, RequestParameters requestParameters) throws UserException, ServerException;
	
	public JsonNode process(HttpRequest request) throws Exception {
		Part part = request.getPart("file");
		RequestParameters requestParameters = new RequestParameters(request, this.getClass().getAnnotation(RequestMapping.class));
		return execute(part, requestParameters);
	}

	public JsonNode processWithRetries(HttpRequest request) throws Exception {
		Random random = new Random();
		for (int i=0; i<10; i++) {
			try {
				return process(request);
			} catch (DeadlockLoserDataAccessException e) {
				LOGGER.info("Deadlock");
				Thread.sleep(i * random.nextInt(500));
			}
		}
		throw new ServerException(ErrorCode.TOO_MANY_DEADLOCKS);
	}
}