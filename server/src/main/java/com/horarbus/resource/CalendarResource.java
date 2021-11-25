package com.horarbus.resource;

import biweekly.ICalendar;
import biweekly.component.VEvent;

import com.horarbus.auth.AuthData;
import com.horarbus.handler.CalendarHandler;
import com.horarbus.handler.UserHandler;
import com.horarbus.service.CalendarService;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.List;

@Path("/calendar")
public class CalendarResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public String get_user_events(@Context RoutingContext context) throws IOException {
        AuthData authData = context.get("authData");
        UserHandler user = new UserHandler(authData.getCip());

        if (!user.is_valid()) {
            user = new UserHandler(authData.getCip(), authData.getLastname(),
                    authData.getFirstname());
            if (!user.is_valid()) {
                return invalidCIP();
            }
        }

        String icalKey = user.get_ical_key();
        if (icalKey == null || icalKey.isEmpty()) {
            return missingIcal();
        }

        ICalendar ical = CalendarService.parseCalendarFromICal(icalKey);
        cacheEventData(ical.getEvents());
        return fetchCalendarData().toString();
    }

    private void cacheEventData(List<VEvent> events) {
        CalendarHandler handler = new CalendarHandler();
        for (int i = 0; i < events.size(); i++) {
            handler.cacheData(events.get(i));
        }
    }

    private JsonObject fetchCalendarData(){
        CalendarHandler handler = new CalendarHandler();
        JsonArray events = handler.getAllEvents();

        JsonObject eventJson = new JsonObject();
        eventJson.put("events", events);
        return eventJson;
    }

    // private String formatEventData(ICalendar iCal) throws IOException {
    // List<VEvent> events = iCal.getEvents();

    // JsonObject[] eventsJson = new JsonObject[events.size()];
    // for (int i = 0; i < events.size(); i++) {
    // eventsJson[i] = CalendarService.parseEvent(events.get(i));
    // }

    // JsonObject outputJson = new JsonObject();
    // outputJson.put("events", eventsJson);
    // return outputJson.toString();
    // }

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
