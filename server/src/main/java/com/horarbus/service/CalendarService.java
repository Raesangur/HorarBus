package com.horarbus.service;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Location;
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
