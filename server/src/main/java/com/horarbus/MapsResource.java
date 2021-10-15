package com.horarbus;

import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/maps")
public class MapsResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/api")
    public String get_api_key() {
        return "-- INSERT API KEY HERE --";
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/defaultLat")
    public String get_default_lat() {
        return "45.3743085028";
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/defaultLng")
    public String get_default_lng() {
        return "-71.9232596403";
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/defaultZoom")
    public String get_default_zoom() {
        return "10";
    }
}
