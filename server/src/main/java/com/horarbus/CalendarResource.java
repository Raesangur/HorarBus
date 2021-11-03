package com.horarbus;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.json.JCalWriter;
import biweekly.property.Location;

import javax.ws.rs.*;
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
    @Produces(MediaType.TEXT_XML)
    @Path("/all")
    public String get_ical() throws IOException{

        ICalendar ical = generateICal(url);


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
        ICalendar ical = generateICal(url);

        ICalendar temp = new ICalendar();
        temp.addEvent(ical.getEvents().get(event));

        Writer result = new StringWriter();
        JCalWriter jcw = new JCalWriter(result);
        jcw.write(temp);

        return result.toString();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/auth")
    public String get_authorization(@HeaderParam("Authorization") String auth) {
        return "";
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        Request request = new Request.Builder()
//                .url("http://10.238.71.75:8888/calendar/auth")
//                .method("GET", null)
//                .addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIwQjhvYUVzRWM5UGE2SVpkWHEwazF3TU0tRXAyMzJ5V1pIOWpDZGFKQmRrIn0.eyJleHAiOjE2MzU4ODM0MDYsImlhdCI6MTYzNTg4MzEwNiwiYXV0aF90aW1lIjoxNjM1ODc4MzA3LCJqdGkiOiI1NzVhNTA3NC00NmUzLTRmNjEtOWY3Mi02ZDNhNjI5MjYwYTciLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3VzYWdlciIsInN1YiI6ImM2ZjljYWNkLTI3MDMtNGExZS1iODdiLWNjNWQ1NjFhMzE1OSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImZyb250ZW5kIiwibm9uY2UiOiIyMTM2MTk4MS1jODhkLTQ5NTYtODdhZS1iNjM3YmQ0M2VkYmMiLCJzZXNzaW9uX3N0YXRlIjoiNTRlMDU3MTEtYzM2Ni00ODJhLWJkNjgtYjdhOTY3MDE3Y2FkIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW1hc3RlciIsInN0dWRlbnQiXX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJzaWQiOiI1NGUwNTcxMS1jMzY2LTQ4MmEtYmQ2OC1iN2E5NjcwMTdjYWQiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJQYXNjYWwtRW1tYW51ZWwgTGFjaGFuY2UiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJsYWNwMzEwMiIsImdpdmVuX25hbWUiOiJQYXNjYWwtRW1tYW51ZWwiLCJmYW1pbHlfbmFtZSI6IkxhY2hhbmNlIiwiZW1haWwiOiJsYWNwMzEwMkB1c2hlcmJyb29rZS5jYSJ9.n6X8E7Sl1lg1XfGxWyFCiFriaUglLcqBqery8Sm9XeNWqxjU1aZ14u5mlYBVfXoNbeHgdg9yozFxZ8A1NzfCCgB_qUMkwBOc5eUX6wG19-ah1g5X9Z32hg0MA_KRBBnDFT0_Q8kCgp8SCFieSXsYadK7TsqbvV3HHmLde_3b3Rap4mT0OWyuzrAnJlJcnatnMyRBG2bj0rSN6dOUkuS7aCd9K-romu6FmD5omEyfQNsYYez7eGCv3DIYQf_BQdo7jqISA-5srO55_guO3KK35umGXfC7onCEhtCOdyyBr_doA76zyGN7mkXbgr6cMnHDPxBvVMYV7B52oCc3YmahZg")
//                .build();
//        Response response = client.newCall(request).execute();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/events")
    public String get_user_events(@DefaultValue("") @QueryParam("user") String user) throws IOException {
        if (user == "") {
            System.out.println("Invalid CIP: No input");
            return "";
        }
        user = user.toLowerCase().strip();
        if (user.length() != 8) {   // cip: abcd1234
            System.out.println("Invalid CIP: Invalid length");
            return "";
        }

        if (!Character.isLetter(user.charAt(0)) ||
            !Character.isLetter(user.charAt(1)) ||
            !Character.isLetter(user.charAt(2)) ||
            !Character.isLetter(user.charAt(3)) ||
            !Character.isDigit(user.charAt(4))  ||
            !Character.isDigit(user.charAt(5))  ||
            !Character.isDigit(user.charAt(6))  ||
            !Character.isDigit(user.charAt(7))) {
                System.out.println("Invalid CIP: Invalid format");
                return "";
        }

        System.out.println("Getting key for user: " + user);
        UserService us = new UserService(user);
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