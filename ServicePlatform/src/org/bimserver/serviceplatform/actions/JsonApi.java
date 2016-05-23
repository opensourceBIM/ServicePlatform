package org.bimserver.serviceplatform.actions;

import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.actionmgmt.PostAction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RequestMapping("/api")
public class JsonApi extends PostAction {

	@Override
	public JsonNode process(JsonNode request) throws Exception {
		if (request.has("request")) {
			ObjectNode requestNode = (ObjectNode) request.get("request");
			String interfaceName = requestNode.get("interface").asText();
			String methodName = requestNode.get("method").asText();
			if (interfaceName.equals("RemoteServiceInterface")) {
				JsonNode result = null;
				if (methodName.equals("getService")) {
					result = getService(requestNode);
				} else if (methodName.equals("getPublicProfiles")) {
					
				} else if (methodName.equals("getPrivateProfiles")) {
					result = getPrivateProfiles(requestNode);
				}
				ObjectNode response = createObjectNode();
				response.set("result", result);
				ObjectNode finalResponse = createObjectNode();
				finalResponse.set("response", response);
				return finalResponse;
			}
		}
		return null;
	}

	private JsonNode getPrivateProfiles(ObjectNode requestNode) {
		ObjectNode parameters = (ObjectNode) requestNode.get("parameters");
		String identifier = parameters.get("serviceIdentifier").asText();
		String code = parameters.get("token").asText();
		System.out.println("Code: " + code);
		// TODO check given code
		
		ArrayNode profiles = createArrayNode();
		
		if (identifier.equals("test")) {
			ObjectNode profile1 = createObjectNode();
			profile1.put("__type", "SProfileDescriptor");
			profile1.put("identifier", "p1");
			profile1.put("name", "Profile 1");
			profile1.put("description", "Test profile");
			profile1.put("public", false);
			profiles.add(profile1);

			ObjectNode profile2 = createObjectNode();
			profile2.put("__type", "SProfileDescriptor");
			profile2.put("identifier", "p2");
			profile2.put("name", "Profile 2");
			profile2.put("description", "Test profile");
			profile2.put("public", false);
			profiles.add(profile2);
		}
		return profiles;
	}

	private JsonNode getService(ObjectNode requestNode) {
		ObjectNode parameters = (ObjectNode) requestNode.get("parameters");
		String identifier = parameters.get("serviceIdentifier").asText();
		if (identifier.equals("test")) {
			ObjectNode result = createObjectNode();
			
			result.put("__type", "SServiceDescriptor");
			result.put("name", "Test Service");
			result.put("identifier", "test");
			result.put("provider", "BIMserver");
			result.put("description", "This is a test service");
			result.put("notificationProtocol", "JSON");
			result.put("trigger", "NEW_REVISION");
			result.put("url", "http://localhost/api");
			result.put("companyUrl", "http://www.bimserver.org");
			result.put("tokenUrl", "");
			result.put("registerUrl", "http://localhost/oauth/register");
			result.put("authorizeUrl", "http://localhost/api/authz");
			result.put("newProfileUrl", "");
			result.put("providerName", "BIMserver");
			
			ObjectNode rights = createObjectNode();
			rights.put("readRevision", true);
			rights.putNull("readExtendedData");
			rights.put("writeRevision", true);
			rights.putNull("writeExtendedData");
			result.set("rights", rights);
			
			return result;
		}
		return null;
	}
}
