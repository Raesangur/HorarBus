package com.horarbus.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.horarbus.auth.AuthData;
import com.horarbus.handler.UserHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@Path("/user")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public String getUserData(@Context RoutingContext context) {
        AuthData authData = context.get("authData");

        UserHandler user = new UserHandler(authData.getCip());
        if (!user.is_valid()) {
            return invalidCIP();
        }

        JsonObject userData = new JsonObject();
        userData.put("firstname", authData.getFirstname());
        userData.put("lastname", authData.getLastname());
        userData.put("preferences", user.get_preferences());

        return userData.toString();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response setUserData(@Context RoutingContext context) {
        // TODO
        return Response.status(200).build();
    }

    // TODO: Join w/ calendar resource (same error messages / functions)
    private String invalidCIP() {
        return sendError("Invalid CIP associated with the current user.");
    }

    private String sendError(String errorMessage) {
        JsonObject errorResponse = new JsonObject();
        errorResponse.put("error", errorMessage);
        return errorResponse.toString();
    }

}
