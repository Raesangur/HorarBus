package com.horarbus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

@Path("/hello")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        try {
            String apiKey = getGoogleApiKey();
            return "Hello RESTEasy" + apiKey;
        } catch (Exception ex) {
            return "Could not find the google key";
        }
    }

    private String getGoogleApiKey() throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("config.properties");
        if (stream != null) {
            try {
                InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.split("=")[0].equals("googleApiKey")) {
                        return line.split("=")[1];
                    }
                }
            } catch (IOException ex) {
                throw new Exception(ex);
            }
        }
        throw new Exception("Could not load config.properties.");
    }
}