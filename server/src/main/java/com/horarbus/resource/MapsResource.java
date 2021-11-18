package com.horarbus.resource;

import com.horarbus.config.Config;
import com.horarbus.config.ConfigException;
import io.vertx.core.json.JsonObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/maps")
public class MapsResource {

    final String DEFAULT_LATITUDE = "45.3743085028";
    final String DEFAULT_LONGITUDE = "-71.9232596403";
    final String DEFAULT_ZOOM = "10";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public String getSettings() {
        JsonObject requestedData = new JsonObject();

        requestedData.put("latitude", DEFAULT_LATITUDE);
        requestedData.put("longitude", DEFAULT_LONGITUDE);
        requestedData.put("zoom", DEFAULT_ZOOM);

        return requestedData.toString();
    }
}
