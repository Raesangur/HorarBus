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
import java.util.Properties;
import java.util.Scanner;

import java.sql.*;


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
    

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/database")
    public String get_database() {
        // https://forum.katalon.com/t/java-sql-sqlexception-no-suitable-driver-found-for-jdbc-localhost-5433/30308
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // https://docs.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver15
        String connectionUrl =  "jdbc:postgresql://localhost:5433/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");

        String val = "<p>";
        ResultSet rs = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl, props);
             Statement statement = connection.createStatement();) {

            System.out.println("Established connection!");
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * FROM Etudiant";
            rs = statement.executeQuery(selectSql);

            // Extract number of columns & adds them to the return string
            // https://stackoverflow.com/a/24946881
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                val += metadata.getColumnName(i) + "\t|\t";
            }
            val += "</p>";

            // Print results from select statement
            while (rs.next()) {
                //String temp = rs.getString(2) + " " + rs.getString(3);
                val += "<p>";
                for (int i = 1; i <= columnCount; i++) {
                    String temp = rs.getString(i);
                    val += temp + "\t|\t";
                    System.out.println(temp);
                }
                val += "</p>";
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return val;
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