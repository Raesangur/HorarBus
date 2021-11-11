package com.horarbus.resource;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Location;
import com.horarbus.auth.AuthData;

import com.horarbus.handler.UserHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

@Path("/calendar")
public class CalendarResource {

    // Get the ical file
    // https://stackoverflow.com/a/13632114
    private ICalendar generateICal(String ical_url) {
        try {
            Scanner scanner = new Scanner(new URL(ical_url).openStream(), "UTF-8");
            String raw = scanner.useDelimiter("\\A").next();
            scanner.close();

            return Biweekly.parse(raw).first();
        } catch (IOException ex) {
            return new ICalendar();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public String get_user_events(@Context RoutingContext context) throws IOException {
        AuthData authData = context.get("authData");
        UserHandler user = new UserHandler(authData.getCip());

        if (!user.is_valid()) {
            return invalidCIP();
        }

        String icalKey = user.get_ical_key();
        if (icalKey == null || icalKey.isEmpty()) {
            return missingIcal();
        }

        ICalendar ical = generateICal(icalKey);
        return formatEventData(ical);
    }

    private String formatEventData(ICalendar iCal) throws IOException {
        List<VEvent> events = iCal.getEvents();

        JsonObject[] eventsJson = new JsonObject[events.size()];
        for (int i = 0; i < events.size(); i++) {
            eventsJson[i] = parseEvent(events.get(i));
        }

        JsonObject outputJson = new JsonObject();
        outputJson.put("events", eventsJson);
        return outputJson.toString();
    }

    private JsonObject parseEvent(VEvent event) {
        JsonObject json = new JsonObject();

        json.put("description", event.getDescription().getValue());
        json.put("summary", event.getSummary().getValue());
        json.put("start", event.getDateStart().getValue());
        json.put("end", event.getDateEnd().getValue());
        json.put("color", event.getColor().getValue());

        Location location = event.getLocation();
        if (location != null) {
            json.put("location", location.getValue());
        }

        return json;
    }

    private String missingIcal() {
        return sendError("No iCal key associated with the current user.");
    }

    private String invalidCIP() {
        return sendError("Invalid CIP associated with the current user.");
    }

    private String sendError(String errorMessage) {
        JsonObject errorResponse = new JsonObject();
        errorResponse.put("error", errorMessage);
        return errorResponse.toString();
    }
}
