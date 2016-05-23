package org.bimserver.serviceplatform.actionmgmt;

import java.util.Random;

import org.bimserver.serviceplatform.DeadlockLoserDataAccessException;
import org.bimserver.serviceplatform.ErrorCode;
import org.bimserver.serviceplatform.HttpRequest;
import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.RequestParameters;
import org.bimserver.serviceplatform.ServerException;
import org.bimserver.serviceplatform.UserException;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class DeleteAction extends Action {

	public abstract JsonNode process(RequestParameters request) throws UserException, ServerException;
	
	public JsonNode process(HttpRequest request) throws Exception {
		return process(new RequestParameters(request, this.getClass().getAnnotation(RequestMapping.class)));
	};

	public JsonNode processWithRetries(HttpRequest request) throws Exception {
		for (int i=0; i<10; i++) {
			try {
				return process(request);
			} catch (DeadlockLoserDataAccessException e) {
				Thread.sleep(i * new Random().nextInt(500));
			}
		}
		throw new ServerException(ErrorCode.TOO_MANY_DEADLOCKS);
	}
}