package com.horarbus;

import biweekly.*;
import biweekly.component.*;
import biweekly.io.json.JCalWriter;
import biweekly.property.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Scanner;


@Path("/calendar")
public class CalendarResource {
private String url = "https://www.gel.usherbrooke.ca/horarius/icalendar?key=67a822b8-32c3-4f87-b074-b01295f0c665";

    private ICalendar generateICal() throws IOException{
        // Get the ical file
        // https://stackoverflow.com/a/13632114
        String raw = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();

        // Parse the file
        return Biweekly.parse(raw).first();
    }

    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/all")
    public String get_ical() throws IOException{

        ICalendar ical = generateICal();


        Writer result = new StringWriter();
        JCalWriter jcw = new JCalWriter(result);
        jcw.write(ical);

        // Extract event data
        /* for (VEvent ve : ical.getEvents())
        {
           String veString = extract_data(ve);
           String color = ve.getColor().getValue();

           result += "<hr><div style=\"background-color:" + color + "\">" + veString + "</div><br>";
        } */

        return result.toString();
    }

    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/event")
    public String get_event(@DefaultValue("1") @QueryParam("event") int event) throws IOException {
        ICalendar ical = generateICal();

        ICalendar temp = new ICalendar();
        temp.addEvent(ical.getEvents().get(event));

        Writer result = new StringWriter();
        JCalWriter jcw = new JCalWriter(result);
        jcw.write(temp);

        return result.toString();
    }

    /* private String extract_data(VEvent event)
    {
        String result = "<p><b>Description: </b>" + event.getDescription().getValue() + "</p>";
        result += "<p><b>Summary: </b>" + event.getSummary().getValue() + "</p>";
        result += "<p><b>De: </b>" + event.getDateStart().getValue() + "</p>";
        result += "<p><b>À: </b>" + event.getDateEnd().getValue() + "</p>";

        Location loc = event.getLocation();
        result += "<p><b>Lieu: </b>" + (loc != null ? loc.getValue() : "UNKNOWN") + "</p>";


        return result;
    } */
}