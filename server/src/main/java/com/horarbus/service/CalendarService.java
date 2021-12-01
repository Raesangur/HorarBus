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
        JsonObject traject = new JsonObject();

        String transport = itineraryData.getString("transport");
        JsonObject itinerary = itineraryData.getJsonObject("itinerary");

        JsonArray waypoints = itinerary.getJsonArray("geocoded_waypoints");
        JsonArray routes = itinerary.getJsonArray("routes");
        String status = itinerary.getString("status");

        // TODO: temps avance
        traject.put("temps_avance", -1);
        traject.put("transport", transport);
        traject.put("adresseDestinataire",
                getLocationDetails(waypoints.getJsonObject(0).getString("place_id")));
        traject.put("adresseInitiale",
                getLocationDetails(waypoints.getJsonObject(1).getString("place_id")));

        if (status.equals("OK")) {
            JsonObject leg = routes.getJsonObject(0).getJsonArray("legs").getJsonObject(0);
            output.put("start", leg.getJsonObject("arrival_time").getLong("value") * 1000);
            output.put("end", leg.getJsonObject("departure_time").getLong("value") * 1000);
            traject.put("steps", leg.getJsonArray("steps"));
        } else {
            output.put("start", null);
            output.put("end", null);
        }

        output.put("traject", traject);

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
