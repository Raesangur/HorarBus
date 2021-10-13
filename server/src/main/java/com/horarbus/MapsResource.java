package com.horarbus;

import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/maps/api")
public class MapsResource {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello() {
        return "--ADD AN API KEY--";
    }
}
