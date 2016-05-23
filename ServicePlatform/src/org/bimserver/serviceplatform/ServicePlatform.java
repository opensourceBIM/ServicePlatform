package org.bimserver.serviceplatform;

import java.nio.file.Paths;

public class ServicePlatform {
	private Database database;
	private EmbeddedWebServer embeddedWebServer;
	private TokenIssuer tokenIssuer;
	private static ServicePlatform SERVICE_PLATFORM;

	public static void main(String[] args) {
		ServicePlatform servicePlatform = new ServicePlatform();
		servicePlatform.start();
	}
	
	public ServicePlatform() {
		SERVICE_PLATFORM = this;
	}
	
	public static ServicePlatform getServicePlatform() {
		return SERVICE_PLATFORM;
	}

	private void start() {
		database = new DerbyDatabase(Paths.get("database"));
		if (database.isNew()) {
			database.initDemoData();
		}
		tokenIssuer = new TokenIssuer();
		
		embeddedWebServer = new EmbeddedWebServer(this);
		embeddedWebServer.start();
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public EmbeddedWebServer getEmbeddedWebServer() {
		return embeddedWebServer;
	}
	
	public TokenIssuer getTokenIssuer() {
		return tokenIssuer;
	}
}
