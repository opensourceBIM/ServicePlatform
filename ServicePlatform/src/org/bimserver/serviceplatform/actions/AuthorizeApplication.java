package org.bimserver.serviceplatform.actions;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.ServicePlatform;
import org.bimserver.serviceplatform.User;
import org.bimserver.serviceplatform.actionmgmt.PostAction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RequestMapping("/api/authorize")
public class AuthorizeApplication extends PostAction {

	@Override
	public JsonNode process(JsonNode request) throws Exception {
		Long userId = (Long)getHttpServletRequest().getSession().getAttribute("userid");
		User user = ServicePlatform.getServicePlatform().getDatabase().getUserById(userId);
		if (user != null) {
			OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				ServicePlatform.getServicePlatform().getDatabase().allowApplication(userId, request.get("applicationId").asText());
//				user.storeOneTimeCode(oauthIssuerImpl.authorizationCode(), null);
		}
		ObjectNode result = createObjectNode();
		result.put("url", "/api/authz");
		return result;
	}	
}