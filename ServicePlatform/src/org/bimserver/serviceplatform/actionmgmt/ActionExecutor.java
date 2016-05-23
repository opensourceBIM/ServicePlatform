package org.bimserver.serviceplatform.actionmgmt;

import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.bimserver.serviceplatform.Context;
import org.bimserver.serviceplatform.DataSource;
import org.bimserver.serviceplatform.HttpServletRequestWrapper;
import org.bimserver.serviceplatform.RequestMethod;
import org.bimserver.serviceplatform.ResponseWrapper;
import org.bimserver.serviceplatform.ServerContext;
import org.bimserver.serviceplatform.ServerException;
import org.bimserver.serviceplatform.UserException;
import org.bimserver.serviceplatform.actions.JsonApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ActionExecutor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActionExecutor.class);

	public JsonNode executeRequest(String method, String path, HttpServletRequestWrapper httpRequest, ResponseWrapper responseWrapper, JsonNode requestNode) {
		try {
			Context context = new Context(ServerContext.getServerContext(), httpRequest, responseWrapper);
			Action action = ServerContext.getServerContext().getActionFactory().create(context, RequestMethod.valueOf(method), removeTralingSlash(path));
			ObjectNode responseNode = null;
			try {
				JsonNode resultNode = null;
				if (action instanceof UploadAction) {
					resultNode = ((UploadAction)action).processWithRetries(httpRequest);
				} else if (action instanceof GetAction) {
					resultNode = ((GetAction)action).processWithRetries(httpRequest);
				} else if (action instanceof DeleteAction) {
					resultNode = ((DeleteAction)action).processWithRetries(httpRequest);
				} else if (action instanceof DownloadAction) {
					DataSource dataSource = ((DownloadAction)action).processWithRetries(httpRequest);
					responseWrapper.setHeader("Content-Disposition", "attachment; filename=" + dataSource.getName());
					IOUtils.copy(dataSource.getInputStream(), responseWrapper.getOutputStream());
					return null;
				} else if (action instanceof JsonAction) {
					resultNode = ((JsonAction)action).processWithRetries(requestNode);
				}
				
				if (context.isForwarded()) {
					return null;
				}
				
				responseNode = createResponse("OK", 200);
				if (resultNode != null) {
					if (action instanceof JsonApi) {
						responseNode = (ObjectNode) resultNode;
					} else {
						responseNode.set("data", resultNode);
					}
				}
				return responseNode;
			} catch (RedirectException e) {
				String str = e.getUri().toString();
				LOGGER.info(str);
				responseWrapper.forward(str);
				return null;
			} catch (UserException e) {
				LOGGER.info("", e.getMessage());
				responseNode = createResponse(e.getMessage(), e.getCode());
				return responseNode;
			} catch (ServerException e) {
				LOGGER.error("", e);
				responseNode = createResponse("Unexpected internal server error", 500);
				return responseNode;
			} catch (ActionNotFoundException e) {
				LOGGER.error("", e);
				responseNode = createResponse("Action not found", 404);
				return responseNode;
			} finally {
//				long totalTimeNanos = timeContext.stop();
//				ServerContext.getServerContext().getElasticSearchClient().prepareIndex("requests", "request")
//			        .setSource(jsonBuilder()
//	                    .startObject()
//	                        .field("method", method)
//	                        .field("path", path)
//	                        .field("responsecode", responseNode == null ? null : responseNode.get("code").asInt())
//	                        .field("responsemessage", responseNode == null ? null : responseNode.get("message").asText())
//	                        .field("ip", httpRequest.getRemoteAddr())
//	                        .field("timemillis", totalTimeNanos / 1000000)
//	                        .field("action", action.getClass().getName())
//	                        .field("username", action.getUserName())
//	                        .field("accesstoken", context.getAccessToken())
//	                        .field("contenttype", httpRequest.getContentType())
//	                        .field("requestjson", requestNode == null ? null : requestNode.toString())
//	                        .field("responsejson", responseNode == null ? null : responseNode.toString())
//	                        .field("postDate", new Date())
//	                    .endObject()
//	                  )
//			        .execute()
//			        .actionGet();
			}
		} catch (Throwable e) {
			LOGGER.error("", e);
			return createResponse("Unexpected internal server error", 500);
		}
	}

	private String removeTralingSlash(String value) {
		if (value.contains("?")) {
			value = value.substring(0, value.indexOf("?"));
		}
		if (value.endsWith("/")) {
			return value.substring(0, value.length() - 1);
		}
		return value;
	}
	
	private ObjectNode createResponse(String message, int code) {
		ObjectNode responseNode = new ObjectMapper().createObjectNode();
		responseNode.put("timestamp", new Date().getTime());
		responseNode.put("code", code);
		responseNode.put("message", message);
		return responseNode;
	}
}