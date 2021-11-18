package com.horarbus.service.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpService implements IHttpService {
    protected URL url;
    private String requestMethod;
    private String authToken;

    public HttpService() {
        url = null;
        requestMethod = null;
        authToken = null;
    }

    public void setURL(String urlStr) throws MalformedURLException {
        url = new URL(urlStr);
    }

    public void setRequestMethod(String method) throws Exception {
        if (isRequestMethodValid(method)) {
            this.requestMethod = method;
        } else {
            throw new HttpException("Invalid request method.");
        }
    }

    private boolean isRequestMethodValid(String method) {
        String[] validMethods = new String[] {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

        if (method == null || method.isEmpty()) {
            return false;
        }

        for (String potentialMethod : validMethods) {
            if (method.toUpperCase().trim().equals(potentialMethod)) {
                return true;
            }
        }

        return false;
    }

    public void setAuthToken(String token) throws Exception {
        if (token != null) {
            this.authToken = token;
        } else {
            throw new HttpException("No token has been passed on.");
        }
    }

    public String executeRequest() throws Exception {
        if (url == null) {
            throw new HttpException("Cannot execute a request if there is no endpoint url.");
        }

        URLConnection connection = connect();
        if (connection == null) {
            throw new HttpException("Connection failed.");
        }
        if (((HttpURLConnection) connection).getResponseCode() < 200
                || ((HttpURLConnection) connection).getResponseCode() >= 300) {
            throw new HttpException("Request refused by auth server.");
        }

        return readConnectionResponse(connection);
    }

    private URLConnection connect() {
        URLConnection connection = null;
        try {
            connection = buildConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                ((HttpURLConnection) connection).disconnect();
            }
        }

        return connection;
    }

    protected URLConnection buildConnection() throws IOException, Exception {
        URLConnection connection = url.openConnection();

        if (requestMethod != null) {
            ((HttpURLConnection) connection).setRequestMethod(requestMethod);
        }

        if (authToken != null) {
            ((HttpURLConnection) connection).setRequestProperty("Authorization", authToken);
        }

        connection.setUseCaches(false);
        connection.setDoOutput(true);

        return connection;
    }

    private String readConnectionResponse(URLConnection connection) {
        StringBuilder response = new StringBuilder();

        try {
            InputStream inStream = connection.getInputStream();
            InputStreamReader inReader = new InputStreamReader(inStream);
            BufferedReader reader = new BufferedReader(inReader);

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append("\r");
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return response.toString();
    }
}
