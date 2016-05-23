package org.bimserver.serviceplatform;

import java.nio.file.Paths;

import org.bimserver.serviceplatform.actions.RegistrationEndpoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class EmbeddedWebServer {
	private ServicePlatform servicePlatform;

	public EmbeddedWebServer(ServicePlatform servicePlatform) {
		this.servicePlatform = servicePlatform;
	}

	public void start() {
		Server server = new Server(80);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.setResourceBase(Paths.get("www").toAbsolutePath().toString());
		context.setWelcomeFiles(new String[] { "index.html" });
		server.setHandler(context);

//		ServletHolder servletHolder = new ServletHolder("AuthenticationServlet", new AuthenticationServlet(servicePlatform));
//		context.addServlet(servletHolder, "/auth/*");
		context.addServlet(DefaultServlet.class, "/");
		context.addServlet(MainServlet.class, "/api/*");
		((HashSessionManager)context.getSessionHandler().getSessionManager()).setSessionCookie("serviceplatform_sessionid");
		context.getSessionHandler().getSessionManager().setMaxInactiveInterval(60 * 60); // 1 hour

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/oauth/*");

		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", RegistrationEndpoint.class.getCanonicalName());
		try { 
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
