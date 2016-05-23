package org.bimserver.serviceplatform;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServlet.class);
	private static final long serialVersionUID = 7316860573688736978L;
	private ServicePlatform servicePlatform;

	public AuthenticationServlet(ServicePlatform servicePlatform) {
		this.servicePlatform = servicePlatform;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String redirectUri = "/auth.html";
		try {
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			validateRedirectionURI(oauthRequest);
			OAuthResponse resp = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND).setCode(servicePlatform.getTokenIssuer().authorizationCode()).location(redirectUri).buildQueryMessage();
			response.sendRedirect(resp.getLocationUri());
		} catch (OAuthProblemException ex) {
			try {
				OAuthResponse resp = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(ex).location(redirectUri).buildQueryMessage();
				response.sendRedirect(resp.getLocationUri());
			} catch (OAuthSystemException e) {
				e.printStackTrace();
			}
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}
	}

	private void validateRedirectionURI(OAuthAuthzRequest oauthRequest) {
		// Always pass for now
	}
}
