package com.horarbus;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@Path("/user")
public class UserResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public String getUserData(@Context RoutingContext context){
        JsonObject userData = new JsonObject();

        // TODO: read data
        UserService service = new UserService("temp ici");    
        userData.put("firstname", "John");
        userData.put("lastname", "Doe");
        userData.put("preferences", service.get_preferences());

        return userData.toString();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response setUserData(@Context RoutingContext context){
        // TODO
        return Response.status(200).build();
    }

}
