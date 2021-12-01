package com.horarbus.service;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import com.horarbus.handler.LocationHandler;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Location;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class CalendarService {
    // Get the ical file
    // https://stackoverflow.com/a/13632114
    public static ICalendar parseCalendarFromICal(String ical_url) {
        try {
            Scanner scanner = new Scanner(new URL(ical_url).openStream(), "UTF-8");
            String raw = scanner.useDelimiter("\\A").next();
            scanner.close();

            return Biweekly.parse(raw).first();
        } catch (IOException ex) {
            return new ICalendar();
        }
    }

    public static JsonObject formatItinerary(JsonObject itineraryData) {
        JsonObject output = new JsonObject();

        String transport = itineraryData.getString("transport");
        JsonObject itinerary = itineraryData.getJsonObject("itinerary");

        JsonArray waypoints = itinerary.getJsonArray("geocoded_waypoints");
        JsonArray routes = itinerary.getJsonArray("routes");
        String status = itinerary.getString("status");

        output.put("transport", transport);
        // TODO: temps avance
        output.put("temps_avance", -1);
        output.put("adresseDestinataire",
                getLocationDetails(waypoints.getJsonObject(0).getString("place_id")));
        output.put("adresseInitiale",
                getLocationDetails(waypoints.getJsonObject(1).getString("place_id")));

        JsonArray details =
                status.equals("OK") ? routes.getJsonObject(0).getJsonArray("legs") : null;
        output.put("details", details);

        return output;
    }

    private static JsonObject getLocationDetails(String placeId) {
        LocationHandler handler = new LocationHandler(placeId);

        JsonObject placeInfo = new JsonObject();
        placeInfo.put("place_id", handler.get_id());
        placeInfo.put("coords", handler.get_coords());
        placeInfo.put("address", handler.get_address());

        return placeInfo;
    }

    public static JsonObject parseEvent(VEvent event) {
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
}
