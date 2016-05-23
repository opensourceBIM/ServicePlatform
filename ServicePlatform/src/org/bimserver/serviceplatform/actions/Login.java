package org.bimserver.serviceplatform.actions;

import org.bimserver.serviceplatform.ErrorCode;
import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.ServicePlatform;
import org.bimserver.serviceplatform.User;
import org.bimserver.serviceplatform.UserException;
import org.bimserver.serviceplatform.actionmgmt.PostAction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RequestMapping("/api/login")
public class Login extends PostAction {

	@Override
	public JsonNode process(JsonNode request) throws Exception {
		User user = ServicePlatform.getServicePlatform().getDatabase().getUserByUserName(request.get("username").asText());
		if (user != null) {
			// Note: All this code is just demo code, DO NOT USE PLAINTEXT PASSWORDS IN PRODUCTION!
			if (user.getPassword().equals(request.get("password").asText())) {
				getSession().setAttribute("userid", user.getId());
			} else {
				throw new UserException(ErrorCode.INVALID_LOGIN);
			}
		} else {
			throw new UserException(ErrorCode.INVALID_LOGIN);
		}
		ObjectNode result = createObjectNode();
		result.put("userid", user.getId());
		result.put("username", user.getUsername());
		return result;
	}	
}