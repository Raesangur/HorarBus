package com.horarbus.auth;

import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import com.horarbus.service.http.HttpsService;
import com.horarbus.service.http.IHttpService;
import java.io.*;
import java.net.*;

@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {

    @ConfigProperty(name = "auth.validationEndpoint")
    Instance<String> keycloakValidationEndpoint;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            String token = getAuthToken(requestContext);
            AuthData authData = readAuthDataFromToken(token);
            requestContext.setProperty("authData", authData);
        } catch (AuthException ex) {
            ex.printStackTrace();
            rejectRequest(requestContext);
        }
    }

    private String getAuthToken(ContainerRequestContext context) throws AuthException {
        try {
            return context.getHeaders().get("Authorization").get(0);
        } catch (Exception e) {
            throw new AuthException("Missing auth token");
        }
    }

    private AuthData readAuthDataFromToken(String token) throws AuthException {
        try {
            IHttpService httpService = new HttpsService();
            httpService.setRequestMethod("GET");
            httpService.setURL(keycloakValidationEndpoint.get());
            httpService.setAuthToken(token);
	    String response = httpService.executeRequest();
            return new AuthData(new JsonObject(response));
        } catch (Exception ex) {
            throw new AuthException(ex.getMessage());
        }
    }

    private void rejectRequest(ContainerRequestContext context) {
        try {
            context.setRequestUri(new URI("/unauthorized"));
        } catch (URISyntaxException e) {
            Response invalidRequestResponse = Response.status(Response.Status.BAD_REQUEST)
                    .entity("Something went wrong..").type(MediaType.TEXT_PLAIN).build();
            context.abortWith(invalidRequestResponse);
        }
    }
}
