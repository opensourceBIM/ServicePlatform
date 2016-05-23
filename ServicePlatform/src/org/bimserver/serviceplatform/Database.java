package org.bimserver.serviceplatform;

public interface Database {

	User getUserById(long userId);
	User getUserByUserName(String username);
	void registerApplication(String clientId, String clientSecret, String clientName, String clientUrl, String clientDescription, String redirectURI, String clientIcon);
	Application getApplicationById(String applicationId);
	void initDemoData();
	boolean isNew();
	boolean userAllowsApplication(long userId, String applicationId);
	void allowApplication(long userId, String applicationId);
}