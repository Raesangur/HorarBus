package com.horarbus.auth;

import io.vertx.core.json.JsonObject;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            String token = getAuthToken(requestContext);
            AuthData authData = validateToken(token);
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

    private AuthData validateToken(String token) throws AuthException {
        String validationEndpoint = "http://localhost/auth/realms/usager/protocol/openid-connect/userinfo";
        HttpURLConnection connection = null;

        try {
            URL url = new URL(validationEndpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Length", Integer.toString(token.getBytes(StandardCharsets.UTF_8).length));

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", token);

            InputStream inStream = connection.getInputStream();
            BufferedReader readStream = new BufferedReader(new InputStreamReader(inStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = readStream.readLine()) != null) {
                response.append(line);
                response.append("\r");
            }
            readStream.close();

            JsonObject json = new JsonObject(response.toString());
            return new AuthData(json.getString("preferred_username"), json.getString("given_name"), json.getString("family_name"), json.getString("email"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        throw new AuthException("idk");

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
