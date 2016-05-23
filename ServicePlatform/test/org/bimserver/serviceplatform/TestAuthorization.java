package org.bimserver.serviceplatform;

import java.io.IOException;

import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.ext.dynamicreg.client.OAuthRegistrationClient;
import org.apache.oltu.oauth2.ext.dynamicreg.client.request.OAuthClientRegistrationRequest;
import org.apache.oltu.oauth2.ext.dynamicreg.client.response.OAuthClientRegistrationResponse;
import org.apache.oltu.oauth2.ext.dynamicreg.common.OAuthRegistration;

public class TestAuthorization {
	public static void main(String[] args) {
		try {
			OAuthClientRequest request = OAuthClientRegistrationRequest.location("http://localhost/oauth/register", OAuthRegistration.Type.PUSH).setName("Test Application").setUrl("http://localhost:1234").setDescription("App Description")
					.setIcon("Icon").setRedirectURL("http://localhost:1234").buildJSONMessage();
	
			OAuthRegistrationClient oauthclient = new OAuthRegistrationClient(new URLConnectionClient());
			OAuthClientRegistrationResponse response = oauthclient.clientInfo(request);
			
			System.out.println("Secret: " + response.getClientSecret());

			OAuthClientRequest request2 = OAuthClientRequest.authorizationLocation("http://localhost/api/application/register").setClientId(response.getClientId()).setRedirectURI("http://localhost/redirect").setResponseType(ResponseType.CODE.toString()).setState("state")
					.buildQueryMessage();

			System.out.println(request2.getLocationUri());
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
	}
}