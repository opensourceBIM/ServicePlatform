package org.bimserver.serviceplatform.actionmgmt;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.bimserver.serviceplatform.Context;
import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActionFactory {
	private final Map<Key, Class<? extends Action>> classes = new HashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(ActionFactory.class);

	private static class Key {
		private RequestMethod requestMethod;
		private String path;

		public Key(RequestMethod requestMethod, String path) {
			this.requestMethod = requestMethod;
			this.path = path;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			result = prime * result + ((requestMethod == null) ? 0 : requestMethod.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			if (requestMethod != other.requestMethod)
				return false;
			return true;
		}
	}

	public void add(Class<? extends Action> actionClass) {
		if (Modifier.isAbstract( actionClass.getModifiers())) {
			return;
		}
		RequestMapping requestMapping = actionClass.getAnnotation(RequestMapping.class);
		if (requestMapping != null) {
			RequestMethod requestMethod = getRequestMethod(actionClass);
			String value = requestMapping.value();
			while (value.contains("{") && value.contains("}")) {
				value = value.substring(0, value.indexOf("{")) + value.substring(value.indexOf("}") + 1);
			}
			value = removeTralingSlashes(value);
			Key key = new Key(requestMethod, value);
			classes.put(key, actionClass);
		} else {
			LOGGER.info("No RequestMapping on " + actionClass.getName());
		}
	}

	private String removeTralingSlashes(String value) {
		while (value.endsWith("/")) {
			value = value.substring(0, value.length() - 1);
		}
		return value;
	}

	private RequestMethod getRequestMethod(Class<? extends Action> actionClass) {
		if (PostAction.class.isAssignableFrom(actionClass) || UploadAction.class.isAssignableFrom(actionClass)) {
			return RequestMethod.POST;
		} else if (GetAction.class.isAssignableFrom(actionClass) || DownloadAction.class.isAssignableFrom(actionClass)) {
			return RequestMethod.GET;
		} else if (PutAction.class.isAssignableFrom(actionClass)) {
			return RequestMethod.PUT;
		} else if (DeleteAction.class.isAssignableFrom(actionClass)) {
			return RequestMethod.DELETE;
		}
		return null;
	}

	public Action create(Context context, RequestMethod requestMethod, String path) throws ActionNotFoundException {
		String originalPath = path + "";
		Action action = null;
		Class<? extends Action> clazz = classes.get(new Key(requestMethod, path));
		if (clazz == null) {
			while (path.contains("/")) {
				clazz = classes.get(new Key(requestMethod, path.substring(0, path.lastIndexOf("/"))));
				path = path.substring(0, path.lastIndexOf("/"));
				if (clazz != null) {
					action = create(clazz);
					break;
				}
			}
		} else {
			action = create(clazz);
		}
		if (action == null) {
			throw new ActionNotFoundException(requestMethod + " " + originalPath);
		}
		action.setContext(context);
		return action;
	}

	@SuppressWarnings("unchecked")
	public <T extends Action> T create(Class<T> clazz) throws ActionNotFoundException {
		return (T) createInternally(clazz);
	}

	public abstract Action createInternally(Class<? extends Action> clazz) throws ActionNotFoundException;
}