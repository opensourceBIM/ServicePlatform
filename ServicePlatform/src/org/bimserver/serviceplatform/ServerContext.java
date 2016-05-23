package org.bimserver.serviceplatform;

import org.bimserver.serviceplatform.actionmgmt.ActionExecutor;
import org.bimserver.serviceplatform.actionmgmt.ActionFactory;
import org.bimserver.serviceplatform.actionmgmt.ActionFactoryBuilder;

public class ServerContext {
	private static final ServerContext INSTANCE = new ServerContext();
	private final ActionFactory actionFactory = new ActionFactoryBuilder(new String[]{"org.bimserver.serviceplatform.actions"}).getActionFactory();
	private ActionExecutor actionExecutor = new ActionExecutor();
	
	public ActionFactory getActionFactory() {
		return actionFactory;
	}

	public static void initServerContext(ResourceLoader resourceLoader) throws DatabaseException {
		
	}

	public static ServerContext getServerContext() {
		return INSTANCE;
	}

	public ActionExecutor getActionExecutor() {
		return actionExecutor;
	}

	public void shutdown() {
	}
}
