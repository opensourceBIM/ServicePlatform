package org.bimserver.serviceplatform.actionmgmt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.bimserver.serviceplatform.DeadlockLoserDataAccessException;
import org.bimserver.serviceplatform.ErrorCode;
import org.bimserver.serviceplatform.ServerException;
import org.bimserver.serviceplatform.UserException;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ContainerNode;

public abstract class JsonAction extends Action {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JsonAction.class);

	public abstract JsonNode process(JsonNode request) throws Exception;

	public JsonNode process(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(request.getInputStream(), baos);
		JsonNode requestNode = null;
		if (request.getContentLength() > 0) {
			try {
				requestNode = getObjectMapper().readValue(new ByteArrayInputStream(baos.toByteArray()), ContainerNode.class);
			} catch (JsonMappingException e) {
				LOGGER.info(new String(baos.toByteArray(), com.google.common.base.Charsets.UTF_8));
				LOGGER.info("", e);
				throw new UserException(ErrorCode.INVALID_JSON);
			}
		}
		return process(requestNode);
	};
	
	public JsonNode processWithRetries(JsonNode request) throws Exception {
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