package org.bimserver.serviceplatform.actions;

import org.bimserver.serviceplatform.Application;
import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.ServicePlatform;
import org.bimserver.serviceplatform.actionmgmt.PostAction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RequestMapping("/api/application/details")
public class GetApplicationDetails extends PostAction {

	@Override
	public JsonNode process(JsonNode request) throws Exception {
		String applicationId = request.get("applicationId").asText();
		Application application = ServicePlatform.getServicePlatform().getDatabase().getApplicationById(applicationId);
		ObjectNode result = createObjectNode();
		result.put("name", application.getClientName());
		result.put("description", application.getClientDescription());
		result.put("icon", application.getClientIcon());
		return result;
	}
}