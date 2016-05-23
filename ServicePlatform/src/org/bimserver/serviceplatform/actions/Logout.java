package org.bimserver.serviceplatform.actions;

import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.actionmgmt.PostAction;

import com.fasterxml.jackson.databind.JsonNode;

@RequestMapping("/api/logout")
public class Logout extends PostAction {

	@Override
	public JsonNode process(JsonNode request) throws Exception {
		getContext().getHttpServletRequest().getSession().removeAttribute("userid");
		return null;
	}
}