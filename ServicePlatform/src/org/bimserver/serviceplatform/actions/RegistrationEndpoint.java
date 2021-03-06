package org.bimserver.serviceplatform.actions;

import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.ext.dynamicreg.server.request.JSONHttpServletRequestWrapper;
import org.apache.oltu.oauth2.ext.dynamicreg.server.request.OAuthServerRegistrationRequest;
import org.apache.oltu.oauth2.ext.dynamicreg.server.response.OAuthServerRegistrationResponse;
import org.bimserver.serviceplatform.Database;
import org.bimserver.serviceplatform.ServicePlatform;

@Path("/register")
public class RegistrationEndpoint {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response register(@Context HttpServletRequest request) throws OAuthSystemException {
        OAuthServerRegistrationRequest oauthRequest = null;
        try {
            oauthRequest = new OAuthServerRegistrationRequest(new JSONHttpServletRequestWrapper(request));
            oauthRequest.discover();
            oauthRequest.getClientName();
            oauthRequest.getClientUrl();
            oauthRequest.getClientDescription();
            oauthRequest.getClientIcon();
            oauthRequest.getRedirectURI();

            Database database = ServicePlatform.getServicePlatform().getDatabase();
            String clientSecret = new MD5Generator().generateValue();
            String clientId = oauthRequest.getClientName().toLowerCase().replace(" ", "");

            database.registerApplication(clientId, clientSecret, oauthRequest.getClientName(), oauthRequest.getClientUrl(), oauthRequest.getClientDescription(), oauthRequest.getRedirectURI(), oauthRequest.getClientIcon());
            
			GregorianCalendar issueDate = new GregorianCalendar();
			GregorianCalendar expireDate = new GregorianCalendar();
			
            OAuthResponse response = OAuthServerRegistrationResponse
                .status(HttpServletResponse.SC_OK)
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setIssuedAt("" + issueDate.getTimeInMillis())
                .setExpiresIn(expireDate.getTimeInMillis())
                .buildJSONMessage();
            return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
        } catch (OAuthProblemException e) {
            OAuthResponse response = OAuthServerRegistrationResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .error(e)
                .buildJSONMessage();
            return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
        }
    }
}