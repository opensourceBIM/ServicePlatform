package org.bimserver.serviceplatform;

import com.fasterxml.jackson.databind.JsonNode;

public class Application {

	private String clientName;
	private String clientUrl;
	private String clientDescript1ion;
	private String redirectURI;
	private String clientIcon;
	private String id;
	private String clientSecret;

	public Application(String id, String clientSecret, String clientName, String clientUrl, String clientDescription, String redirectURI, String clientIcon) {
		this.setId(id);
		this.setClientSecret(clientSecret);
		this.setClientName(clientName);
		this.setClientUrl(clientUrl);
		this.setClientDescript1ion(clientDescription);
		this.setRedirectURI(redirectURI);
		this.setClientIcon(clientIcon);
	}

	public Application() {
	}

	public String getId() {
		return id;
	}

	public String getClientDescription() {
		return getClientDescript1ion();
	}
	
	public String getClientIcon() {
		return clientIcon;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public String getClientSecret() {
		return clientSecret;
	}
	
	public String getClientUrl() {
		return clientUrl;
	}
	
	public String getRedirectURI() {
		return redirectURI;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}

	public String getClientDescript1ion() {
		return clientDescript1ion;
	}

	public void setClientDescript1ion(String clientDescript1ion) {
		this.clientDescript1ion = clientDescript1ion;
	}

	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}

	public void setClientIcon(String clientIcon) {
		this.clientIcon = clientIcon;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
}
