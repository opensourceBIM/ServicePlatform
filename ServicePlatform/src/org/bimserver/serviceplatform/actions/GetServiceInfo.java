package org.bimserver.serviceplatform.actions;

import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.actionmgmt.PostAction;

import com.fasterxml.jackson.databind.JsonNode;

@RequestMapping("/api/getserviceinfo")
public class GetServiceInfo extends PostAction {

	@Override
	public JsonNode process(JsonNode request) throws Exception {
		String serviceIdentifier = request.get("serviceIdentifier").asText();
		
		return null;
	}
}