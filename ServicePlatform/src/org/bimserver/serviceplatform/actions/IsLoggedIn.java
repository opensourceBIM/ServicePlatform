package org.bimserver.serviceplatform.actions;

import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.ServicePlatform;
import org.bimserver.serviceplatform.User;
import org.bimserver.serviceplatform.actionmgmt.PostAction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RequestMapping("/api/isloggedin")
public class IsLoggedIn extends PostAction {

	@Override
	public JsonNode process(JsonNode request) throws Exception {
		Object attribute = getContext().getHttpServletRequest().getSession().getAttribute("userid");
		ObjectNode result = createObjectNode();
		if (attribute != null) {
			Long userid = (Long)attribute;
			User user = ServicePlatform.getServicePlatform().getDatabase().getUserById(userid);

			result.put("loggedin", true);
			result.put("userid", userid);
			result.put("username", user.getUsername());
		} else {
			result.put("loggedin", false);
		}
		return result;
	}
}
