package com.horarbus.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.horarbus.UserPrefs;
import com.horarbus.auth.AuthData;
import com.horarbus.handler.*;
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

        UserPrefs prefs = new UserPrefs(user);
        JsonObject userData = new JsonObject();
        userData.put("firstname", authData.getFirstname());
        userData.put("lastname", authData.getLastname());
        userData.put("preferences", prefs.toJson());

        return userData.toString();
    }

    // @GET
    // @Produces(MediaType.TEXT_HTML)
    // @Path("/events")
    // public String getUserEvents(@Context RoutingContext context) {
    // AuthData authData = context.get("authData");

    // UserHandler user = new UserHandler(authData.getCip());
    // if (!user.is_valid()) {
    // return invalidCIP();
    // }

    // EventHandler[] events = user.get_events();

    // String result = new String();
    // for (EventHandler event : events) {
    // result += event.get_id() + " " + event.get_name() + " : \n";
    // }

    // return result;
    // }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response setUserData(@Context RoutingContext context, JsonObject sentData) {
        AuthData authData = context.get("authData");

        UserHandler userHandler = new UserHandler(authData.getCip());
        if (!userHandler.is_valid()) {
            return Response.status(400).entity(invalidCIP()).build();
        }

        try {
            UserPrefs prefs = new UserPrefs(sentData);
            prefs.saveData(userHandler);
        } catch (Exception ex) {
            return Response.status(400).entity(invalidJson()).build();
        }

        return Response.status(200).build();
    }

    // TODO: Join w/ calendar resource (same error messages / functions)
    private String invalidCIP() {
        return sendError("Invalid CIP associated with the current user.");
    }

    private String invalidJson() {
        return sendError("Invalid JSON associated with the request.");
    }

    private String sendError(String errorMessage) {
        JsonObject errorResponse = new JsonObject();
        errorResponse.put("error", errorMessage);
        return errorResponse.toString();
    }

}
