package com.horarbus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.horarbus.service.MapsService;

@Path("/unauthorized")
public class UnauthorizedResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUnauthorized() {
        String res = "";

        try {
            res = MapsService.getPlaceDataFromId("ChIJ3zeuCDmzt0wR-OmeoeEEspM");
        } catch (Exception ex) {
            res = "An error occured.";
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity(res).type(MediaType.TEXT_PLAIN)
                .build();
    }
}
