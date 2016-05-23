package org.bimserver.serviceplatform;

import java.util.HashMap;
import java.util.Map;

public class User {

	private long id;
	private String username;
	private String password;
	private Map<String, String> clientIdToOneTimeCode = new HashMap<String, String>();
	
	public User(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public User() {
	}

	public String getPassword() {
		return password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public boolean allowsClient(String clientId) {
		return clientIdToOneTimeCode.containsKey(clientId);
	}

	public void storeOneTimeCode(String clientId, String oneTimeCode) {
		clientIdToOneTimeCode.put(clientId, oneTimeCode);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
