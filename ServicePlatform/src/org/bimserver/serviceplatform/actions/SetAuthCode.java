package org.bimserver.serviceplatform.actions;

import org.bimserver.serviceplatform.RequestParameters;
import org.bimserver.serviceplatform.ServerException;
import org.bimserver.serviceplatform.UserException;
import org.bimserver.serviceplatform.actionmgmt.GetAction;
import org.bimserver.serviceplatform.actionmgmt.RedirectException;

import com.fasterxml.jackson.databind.JsonNode;

public class SetAuthCode extends GetAction {

	@Override
	public JsonNode process(RequestParameters request) throws UserException, ServerException, RedirectException {
//		User user = ServicePlatform.getServicePlatform().getDatabase().getUserByUserName(request.get("username").asText());
		return null;
	}
}