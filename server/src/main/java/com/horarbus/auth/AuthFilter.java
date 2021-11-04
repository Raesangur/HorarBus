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

    private AuthData readAuthDataFromToken(String token) throws AuthException, IOException {
        HttpURLConnection connection = getValidationConnection(token);
        if (connection == null) {
            throw new AuthException("Connection failed.");
        }
        if (connection.getResponseCode() < 200 || connection.getResponseCode() >= 300) {
            throw new AuthException("Request refused by auth server.");
        }

        String response = readKeycloakAnswer(connection);
        return new AuthData(new JsonObject(response));
    }

    private HttpURLConnection getValidationConnection(String token) throws AuthException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(keycloakValidationEndpoint.get());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", token);
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return connection;
    }

    private String readKeycloakAnswer(HttpURLConnection connection) throws AuthException {
        StringBuilder response = new StringBuilder();

        try {
            InputStream inStream = connection.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inStream);
            BufferedReader readStream = new BufferedReader(streamReader);

            String line;
            while ((line = readStream.readLine()) != null) {
                response.append(line);
                response.append("\r");
            }
            readStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthException("Could not read response data.");
        }

        return response.toString();
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
