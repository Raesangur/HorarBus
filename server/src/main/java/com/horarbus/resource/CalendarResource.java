package com.horarbus.resource;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.horarbus.MissingTraject;
import com.horarbus.Utils;
import com.horarbus.auth.AuthData;
import com.horarbus.handler.CalendarHandler;
import com.horarbus.handler.UserHandler;
import com.horarbus.service.CalendarService;
import com.horarbus.service.MapsService;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

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

        CalendarHandler handler = new CalendarHandler(authData.getCip());
        ICalendar ical = CalendarService.parseCalendarFromICal(icalKey);
        cacheEventData(handler, ical.getEvents());
        return fetchCalendarData(handler).toString();
    }

    private void cacheEventData(CalendarHandler handler, List<VEvent> events) {
        for (int i = 0; i < events.size(); i++) {
            handler.cacheData(events.get(i));
            handler.associateEventToUser(events.get(i).getUid().getValue().toString());
        }
    }

    private JsonObject fetchCalendarData(CalendarHandler handler) {
        JsonObject eventJson = new JsonObject();
        JsonArray events = handler.getAllEvents();
        eventJson.put("events", events);

        try {
            Set<MissingTraject> missing = handler.getMissingTrajects();
            generateMissingTrajects(handler, missing);
        } catch (Exception ex) {
            ex.printStackTrace();
            eventJson.put("trajects", new JsonArray());
        }

        JsonArray formattedTrajects = new JsonArray();
        HashMap<String, JsonObject> loadedPlaceInfo = new HashMap<String, JsonObject>();
        for (JsonObject itinerary : handler.getAllTrajects()) {
            if (itinerary != null) {
                formattedTrajects.add(
                        CalendarService.formatItinerary((JsonObject) itinerary, loadedPlaceInfo));
            }
        }

        eventJson.put("trajects", formattedTrajects);

        return eventJson;
    }

    private void generateMissingTrajects(CalendarHandler handler, Set<MissingTraject> missing)
            throws Exception {
        for (MissingTraject traject : missing) {
            String itinerary = MapsService.getItinerary(traject.getStartPlaceId(),
                    traject.getEndPlaceId(), traject.getTravelMode(),
                    Utils.timestampToMillis(traject.getArrivalTime()) / 1000);
            handler.registerItinerary(traject, itinerary);
        }
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
