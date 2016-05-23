package org.bimserver.serviceplatform.actions;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.bimserver.serviceplatform.RequestMapping;
import org.bimserver.serviceplatform.RequestParameters;
import org.bimserver.serviceplatform.ServerException;
import org.bimserver.serviceplatform.ServicePlatform;
import org.bimserver.serviceplatform.User;
import org.bimserver.serviceplatform.UserException;
import org.bimserver.serviceplatform.actionmgmt.GetAction;
import org.bimserver.serviceplatform.actionmgmt.RedirectException;

import com.fasterxml.jackson.databind.JsonNode;

@RequestMapping("/api/authz")
public class AuthzEndpoint extends GetAction {

	@Override
	public JsonNode process(RequestParameters request) throws UserException, ServerException, RedirectException {
		try {
			HttpSession session = getHttpServletRequest().getSession();
			User user = null;
			if (session.getAttribute("userid") != null) {
				long userId = (Long) session.getAttribute("userid");
				user = ServicePlatform.getServicePlatform().getDatabase().getUserById(userId);
			}

			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(getHttpServletRequest().getHttpServletRequest());

			if (user == null) {
				final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);
				URI uri = URI.create("/#login?redirect_uri=" + oauthRequest.getRedirectURI() + "&response_type=" + oauthRequest.getResponseType() + "&state=" + oauthRequest.getState() + "&client_id=" + oauthRequest.getClientId()
						+ "&clientSecret=" + oauthRequest.getClientSecret());
				throw new RedirectException(uri);
			}

			OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

			if (!ServicePlatform.getServicePlatform().getDatabase().userAllowsApplication(user.getId(), oauthRequest.getClientId())) {
				final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);
				URI uri = URI.create("/#authorize?redirect_uri=" + oauthRequest.getRedirectURI() + "&response_type=" + oauthRequest.getResponseType() + "&state=" + oauthRequest.getState() + "&client_id=" + oauthRequest.getClientId()
						+ "&clientSecret=" + oauthRequest.getClientSecret());
				throw new RedirectException(uri);
			}

			// build response according to response_type
			String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(getHttpServletRequest().getHttpServletRequest(), HttpServletResponse.SC_FOUND);

			if (responseType.equals(ResponseType.CODE.toString())) {
				String oneTimeCode = oauthIssuerImpl.authorizationCode();
				builder.setCode(oneTimeCode);
				user.storeOneTimeCode(oauthRequest.getClientId(), oneTimeCode);
			}
			if (responseType.equals(ResponseType.TOKEN.toString())) {
				builder.setAccessToken(oauthIssuerImpl.accessToken());
				// builder.setTokenType(OAuth.DEFAULT_TOKEN_TYPE.toString());
				builder.setExpiresIn(3600l);
			}

			String redirectURI = oauthRequest.getRedirectURI();
			try {
				redirectURI = URLDecoder.decode(redirectURI, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
			URI uri = new URI(response.getLocationUri());

			throw new RedirectException(uri);
			// return
			// Response.status(response.getResponseStatus()).location(url).build();
		} catch (OAuthProblemException e) {
			final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);

			String redirectUri = e.getRedirectUri();

			if (OAuthUtils.isEmpty(redirectUri)) {
				throw new WebApplicationException(responseBuilder.entity("OAuth callback url needs to be provided by client!!!").build());
			}
			try {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e).location(redirectUri).buildQueryMessage();
				final URI uri = new URI(response.getLocationUri());
				throw new RedirectException(uri);
			} catch (OAuthSystemException e1) {
				e1.printStackTrace();
			}
			// return responseBuilder.location(location).build();
			catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}