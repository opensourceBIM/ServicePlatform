package org.bimserver.serviceplatform;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bimserver.serviceplatform.actionmgmt.ActionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(value="/api/*", loadOnStartup=1)
@MultipartConfig
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MainServlet.class);
	
	@Override
	public void init() throws ServletException {
		ResourceLoader resourceLoader = new ResourceLoader() {
			@Override
			public InputStream getResource(String name) {
				URL resource;
				try {
					resource = getServletContext().getResource("/WEB-INF/" + name);
					if (resource != null) {
						return resource.openStream();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		try {
			ServerContext.initServerContext(resourceLoader);
		} catch (DatabaseException e) {
			LOGGER.error("", e);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		ServerContext.getServerContext().shutdown();
	}
	
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
			String servletPath = request.getRequestURI();

			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.setHeader("Access-Control-Allow-Headers", "Content-Type");

			if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
				return;
			}
			response.setCharacterEncoding("UTF-8");
			
			String method = request.getMethod();

			if (servletPath.startsWith(request.getServletContext().getContextPath())) {
				servletPath = servletPath.substring(request.getServletContext().getContextPath().length());
			}
			ActionExecutor actionExecutor = ServerContext.getServerContext().getActionExecutor();
			
			response.setContentType("application/json; charset=utf-8");

			HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode requestNode = null;
			if (request.getContentType() != null && request.getContentType().startsWith("application/json")) {
				if (request.getContentLength() > 0) {
					requestNode = objectMapper.readValue(request.getInputStream(), JsonNode.class);
				}
			}
			
			ResponseWrapper responseWrapper = new HttpResponseWrapper(response);
			
			JsonNode responseNode = actionExecutor.executeRequest(method, servletPath, requestWrapper, responseWrapper, requestNode);
			if (responseNode != null) {
				if (responseNode.has("code")) {
					int code = responseNode.get("code").asInt();
					if (code == 500 || code == 404) {
						response.setStatus(code);
					}
				}
				response.getOutputStream().write(objectMapper.writeValueAsBytes(responseNode));
			}
    	} catch (IOException e) {
    		LOGGER.error("IOException, can be ignored");
		} catch (Throwable e) {
			LOGGER.error("", e);
		}
    }
}