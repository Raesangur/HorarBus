package com.horarbus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/unauthorized")
public class UnauthorizedResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUnauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).build();
    }
}
