package com.horarbus;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.json.JCalWriter;
import biweekly.property.Location;
import com.horarbus.auth.AuthData;
import io.vertx.ext.web.RoutingContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

@Path("/calendar")
public class CalendarResource {
private String url = "https://www.gel.usherbrooke.ca/horarius/icalendar?key=67a822b8-32c3-4f87-b074-b01295f0c665";
    private ICalendar generateICal(String ical_url) throws IOException{
        // Get the ical file
        // https://stackoverflow.com/a/13632114
        String raw = new Scanner(new URL(ical_url).openStream(), "UTF-8").useDelimiter("\\A").next();

        // Parse the file
        return Biweekly.parse(raw).first();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/test")
    public String test() {
        PostgresService pgs = new PostgresService();

        pgs.update_column("nom",
                "etudiant",
                "Petit",
                "cip",
                "lacp3102");

        return "";
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/events")
    public String get_user_events(@Context RoutingContext context) throws IOException {
        AuthData authData = context.get("authData");
        String user = authData.getCip();

        System.out.println("Getting key for user: " + user);
        UserService us = new UserService(user);

        if (us.is_valid() == false) {
            return "";
        }
        
        String ical_key = us.get_ical_key();

        ICalendar ical = generateICal(ical_key);


        Writer result = new StringWriter();
        //JCalWriter jcw = new JCalWriter(result);
        //jcw.write(ical);

        // Extract event data
        for (VEvent ve : ical.getEvents())
        {
           String veString = extract_data(ve);
           String color = ve.getColor().getValue();

           result.append("<hr><div style=\"background-color:").append(color).append("\">").append(veString).append("</div><br>");
        }

        return result.toString();
    }

    private String extract_data(VEvent event)
    {
        String result = "<p><b>Description: </b>" + event.getDescription().getValue() + "</p>";
        result += "<p><b>Summary: </b>" + event.getSummary().getValue() + "</p>";
        result += "<p><b>De: </b>" + event.getDateStart().getValue() + "</p>";
        result += "<p><b>À: </b>" + event.getDateEnd().getValue() + "</p>";

        Location loc = event.getLocation();
        result += "<p><b>Lieu: </b>" + (loc != null ? loc.getValue() : "UNKNOWN") + "</p>";


        return result;
    }
}