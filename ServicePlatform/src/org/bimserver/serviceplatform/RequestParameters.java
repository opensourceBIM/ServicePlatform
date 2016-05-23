package org.bimserver.serviceplatform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;

public class RequestParameters {
	private HttpRequest request;
	private final Map<String, String> additionalParameters = new HashMap<>();

	public RequestParameters(HttpRequest request, RequestMapping requestMapping) throws IOException, ServletException {
		this.request = request;
		String value = requestMapping.value();
		String pathinfo = removeTralingSlash(request.getRequestURI());
		while (value.contains("{") && value.contains("}")) {
			int begin = value.lastIndexOf("{");
			String paramName = value.substring(begin + 1, value.lastIndexOf("}"));
			int lastIndexOf = pathinfo.lastIndexOf("/");
			if (lastIndexOf == -1) {
				break;
			}
			String encoded = pathinfo.substring(lastIndexOf + 1);
			pathinfo = pathinfo.substring(0, lastIndexOf);
			try {
				String decoded = URLDecoder.decode(encoded, Charsets.UTF_8.name());
				additionalParameters.put(paramName, decoded);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			value = value.substring(0, begin);
		}

		if ("multipart/form-data".equals(request.getContentType())) {
			Collection<Part> parts = request.getParts();
			for (Part part : parts) {
				if (!part.getName().equals("file")) {
					InputStream inputStream = part.getInputStream();
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					additionalParameters.put(part.getName(), new String(output.toByteArray(), Charsets.UTF_8));
					IOUtils.copy(inputStream, output);
				}
			}
		}
	}
	
	private String removeTralingSlash(String value) {
		if (value.contains("?")) {
			value = value.substring(0, value.indexOf("?"));
		}
		if (value.endsWith("/")) {
			return value.substring(0, value.length() - 1);
		}
		return value;
	}
	
	public String getAdditional(String key) {
		return additionalParameters.get(key);
	}
	
	public String get(String key, String defaultValue) {
		String result = request.getParameter(key);
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	public int get(String key, int defaultValue) {
		String result = request.getParameter(key);
		if (result == null) {
			return defaultValue;
		}
		return Integer.parseInt(result);
	}

	public String get(String key) {
		String value = request.getParameter(key);
		if (value == null) {
			return additionalParameters.get(key);
		}
		return value;
	}

	public boolean has(String fieldName) {
		return request.getParameter(fieldName) != null || additionalParameters.containsKey(fieldName);
	}

	public long getLong(String key) throws UserException {
		try {
			String value = get(key);
			if (value != null) {
				return Long.parseLong(value);
			}
			return 0;
		} catch (NumberFormatException e) {
			throw new UserException(ErrorCode.INVALID_FIELD_VALUE);
		}
	}

	public int getInt(String key, int defaultValue) throws UserException {
		try {
			String value = get(key);
			if (value == null) {
				return defaultValue;
			}
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new UserException(ErrorCode.INVALID_FIELD_VALUE);
		}
	}

	public String getString(String key, String defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public String getString(String key) {
		return get(key);
	}
	
	@Override
	public String toString() {
		return request.toString();
	}

	public byte getByte(String key) throws UserException {
		try {
			String value = get(key);
			return Byte.parseByte(value);
		} catch (NumberFormatException e) {
			throw new UserException(ErrorCode.INVALID_FIELD_VALUE);
		}
	}
}