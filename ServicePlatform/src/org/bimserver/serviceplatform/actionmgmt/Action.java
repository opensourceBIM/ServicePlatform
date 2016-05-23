package org.bimserver.serviceplatform.actionmgmt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.bimserver.serviceplatform.Context;
import org.bimserver.serviceplatform.DatabaseException;
import org.bimserver.serviceplatform.ErrorCode;
import org.bimserver.serviceplatform.HttpServletRequestWrapper;
import org.bimserver.serviceplatform.MissingArgumentException;
import org.bimserver.serviceplatform.RequestParameters;
import org.bimserver.serviceplatform.User;
import org.bimserver.serviceplatform.UserException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract class Action {
	private Context context;

	public void setContext(Context context) {
		this.context = context;
	}

	public <T extends Action> T createAction(Class<T> clazz) throws ActionNotFoundException {
		T action = context.getActionFactory().create(clazz);
		action.setContext(context);
		return action;
	}

	public HttpServletRequestWrapper getHttpServletRequest() {
		return context.getHttpServletRequest();
	}

	public ObjectMapper getObjectMapper() {
		return context.getObjectMapper();
	}

	public void forward(String url) throws IOException {
		context.forward(url);
	}
	
	public Context getContext() {
		return context;
	}

	public HttpSession getSession() {
		return context.getHttpServletRequest().getSession();
	}
	
	public ArrayNode createArrayNode() {
		return getContext().getObjectMapper().createArrayNode();
	}
	
	public void checkHasFields(JsonNode request, String... fieldNames) throws MissingArgumentException {
		if (request == null) {
			throw new MissingArgumentException("No request at all");
		}
		for (String fieldName: fieldNames) {
			if (!request.has(fieldName)) {
				throw new MissingArgumentException(fieldName);
			}
		}
	}
	
	public void checkHasFields(RequestParameters requestParameters, String... fieldNames) throws MissingArgumentException {
		for (String fieldName: fieldNames) {
			if (!requestParameters.has(fieldName)) {
				throw new MissingArgumentException(fieldName);
			}
		}
	}
	
	public ObjectNode success() {
		return context.getObjectMapper().createObjectNode();
	}

	public User requireAppUser() throws UserException {
		User user = requireLoggedIn();
		if (!(user instanceof User)) {
			throw new UserException(ErrorCode.INSUFFICIENT_RIGHTS);
		}
		return (User) user;
	}

	public User requireUser() throws UserException {
		return requireLoggedIn();
	}

	public JsonNode toJson(Object object) {
		return context.getObjectMapper().convertValue(object, JsonNode.class);
	}

	public ObjectNode createObjectNode() {
		return context.getObjectMapper().createObjectNode();
	}

	protected <T> List<T> mapJsonToObjectList(T typeDef, JsonNode json, Class<? extends T> clazz) {
		List<T> list;
		TypeFactory t = TypeFactory.defaultInstance();
		list = getContext().getObjectMapper().convertValue(json, t.constructCollectionType(ArrayList.class, clazz));
		return list;
	}

	private User requireLoggedIn() throws UserException {
		String accessToken = getContext().getAccessToken();
		if (accessToken == null) {
			throw new UserException(ErrorCode.ACCESS_TOKEN_REQUIRED);
		}
		User user;
		try {
			user = getContext().getTokenManager().get(accessToken);
		} catch (DatabaseException e) {
			throw new UserException(ErrorCode.DATABASE_ERROR);
		}
		if (user == null) {
			throw new UserException(ErrorCode.INVALID_ACCESS_TOKEN);
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fromJson(JsonNode request, Class<T> class1, Object object) {
		return (List<T>) mapJsonToObjectList(object, request, class1);
	}
	
	public User getUser() throws UserException {
		try {
			String accessToken = getContext().getAccessToken();
			if (accessToken == null) {
				return null;
			}
			User user = getContext().getTokenManager().get(accessToken);
			if (user == null) {
				return null;
			}
			return ((User)user);
		} catch (UserException e) {
			// die
		} catch (DatabaseException e) {
			throw new UserException(ErrorCode.DATABASE_ERROR);
		}
		return null;
	}

	public String getUserName() throws UserException {
		try {
			User user = getUser();
			return ((User)user).getUsername();
		} catch (UserException e) {
			// die
		}
		return null;
   	}
}