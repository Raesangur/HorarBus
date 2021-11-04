package com.horarbus.auth;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // TODO: filter keycloak

        try {
            System.out.println("TOKEN: " + getAuthToken(requestContext));

            AuthData authData = new AuthData("rouj1615", "Julien", "S!X");
            requestContext.setProperty("authData", authData);
        } catch (AuthException ex) {
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

    private void rejectRequest(ContainerRequestContext context) {
        try {
            context.setRequestUri(new URI("/unauthorized"));
        } catch (URISyntaxException e) {
            Response invalidRequestResponse = Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Something went wrong..")
                    .type(MediaType.TEXT_PLAIN).build();
            context.abortWith(invalidRequestResponse);
        }
    }
}
