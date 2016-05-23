package org.bimserver.serviceplatform;

import java.io.IOException;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.ext.dynamicreg.client.OAuthRegistrationClient;
import org.apache.oltu.oauth2.ext.dynamicreg.client.request.OAuthClientRegistrationRequest;
import org.apache.oltu.oauth2.ext.dynamicreg.client.response.OAuthClientRegistrationResponse;
import org.apache.oltu.oauth2.ext.dynamicreg.common.OAuthRegistration;

public class Test {
	public static void main(String[] args) {
		String accessTokenEndpoint = "http://localhost/register";
		String authorizationCode = "";
		String redirectUrl = "http://localhost";
		String clientId = "";
		String clientSecret = "";

		String registrationEndpoint = "http://localhost/register";

		OAuthClientRequest request;
		try {
			request = OAuthClientRegistrationRequest.location(registrationEndpoint, OAuthRegistration.Type.PUSH).setName("Test").setUrl("http://test").setDescription("test").setIcon("test").setRedirectURL("http://test")
					.buildJSONMessage();
			OAuthRegistrationClient oauthclient = new OAuthRegistrationClient(new URLConnectionClient());
			OAuthClientRegistrationResponse response = oauthclient.clientInfo(request);
			System.out.println(response.getExpiresIn());
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
	}
}
