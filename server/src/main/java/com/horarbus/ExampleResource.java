package com.horarbus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Path("/hello")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        try{
            String apiKey = getGoogleApiKey();
            return "Hello RESTEasy" + apiKey;
        }catch (Exception ex){
            return "Could not find the google key";
        }
    }

    private String getGoogleApiKey() throws Exception{
        try{
            File envFile = new File("../../../../.env");
            Scanner fileReader = new Scanner(envFile);
            while(fileReader.hasNextLine()){
                String envVar = fileReader.nextLine();
                String[] envVarParts = envVar.split("=");

                if(envVarParts[0].equals("GOOGLE_API_KEY")){
                    return envVarParts[1];
                }
            }
            fileReader.close();
            throw new Exception("Could not find the API key");
        }catch (FileNotFoundException ex){
            throw new Exception(ex);
        }
    }
}