package com.horarbus.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import com.google.maps.model.TravelMode;
import com.horarbus.Location;
import com.horarbus.MissingTraject;
import com.horarbus.Utils;
import com.horarbus.service.CalendarService;
import com.horarbus.service.MapsService;

import biweekly.component.VEvent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class CalendarHandler {
    private PostgresHandler pgh = null;
    private String cip;

    public CalendarHandler(String userCip) {
        pgh = new PostgresHandler();
        this.cip = userCip;
    }

    public JsonArray getAllEvents() {
        ResultSet results = executeRequestForUser("calendarattendance");

        try {
            JsonArray events = new JsonArray();
            while (results.next()) {
                VEvent event = new VEvent();
                event.setDescription(results.getString("description"));
                event.setSummary(results.getString("summary"));
                event.setDateStart(results.getTimestamp("start_time"), true);
                event.setDateEnd(results.getTimestamp("end_time"), true);
                event.setColor(results.getString("color"));
                event.setLocation(results.getString("local"));
                events.add(CalendarService.parseEvent(event));
            }
            return events;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Set<MissingTraject> getMissingTrajects() throws Exception {
        ResultSet results = executeRequestForUser("missingTraject");

        try {
            Set<MissingTraject> missing = new HashSet<MissingTraject>();
            while (results.next()) {
                String transportStr = results.getString("requestedtransport");
                if (transportStr == null || transportStr.isEmpty()) {
                    throw new Exception("No transport mode has been defined.");
                }

                TravelMode transport = TravelMode.valueOf(transportStr);
                Timestamp eventStartTime = results.getTimestamp("start_time");

                missing.add(new MissingTraject(results.getString("startplace"),
                        results.getString("targetplace"), transport, eventStartTime));
            }

            return missing;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<JsonObject> getAllTrajects() {
        ResultSet results = executeRequestForUser("usertrajectevent");

        try {
            ArrayList<JsonObject> trajects = new ArrayList<>();
            while (results.next()) {
                MissingTraject traject;

                String startPlaceId = results.getString("start_place_id");
                String endPlaceId = results.getString("end_place_id");
                TravelMode transport = TravelMode.valueOf(results.getString("transport_name"));

                Timestamp arrivalTime = results.getTimestamp("event_start_time");
                traject = new MissingTraject(startPlaceId, endPlaceId, transport, arrivalTime);

                String itineraryJson = readFile(traject.getFilename());
                if (itineraryJson != null) {
                    JsonObject data = new JsonObject();
                    data.put("transport", transport.toString());
                    data.put("eventTime", Utils.timestampToMillis(arrivalTime));
                    data.put("itinerary", new JsonObject(itineraryJson));
                    trajects.add(data);
                }
            }
            return trajects;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void cacheData(VEvent eventData) {
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("event_id");
        columns.add("start_time");
        columns.add("end_time");
        columns.add("description");
        columns.add("summary");

        ArrayList<PostgresValue> values = new ArrayList<PostgresValue>();
        values.add(new PostgresValue(Integer.parseInt(eventData.getUid().getValue())));
        values.add(new PostgresValue(
                Utils.getTimeFromString(eventData.getDateStart().getValue().toString())));
        values.add(new PostgresValue(
                Utils.getTimeFromString(eventData.getDateEnd().getValue().toString())));
        values.add(new PostgresValue(eventData.getDescription().getValue().toString()));
        values.add(new PostgresValue(eventData.getSummary().getValue().toString()));

        if (eventData.getColor() != null) {
            columns.add("color");
            values.add(new PostgresValue(eventData.getColor().getValue().toString()));
        }

        // TODO: optimize
        if (eventData.getLocation() != null) {
            String location = eventData.getLocation().getValue().toString();
            Pattern pattern = Pattern.compile("[a-zA-Z]{1}[1-2]{1}-[0-9]+");

            // utiliser force refresh? ne pas fetch google si on a deja l'info dans la bd?
            if (pattern.matcher(location).matches()) {
                columns.add("local");
                values.add(new PostgresValue(location));
                columns.add("place_id");
                // Faculté de Génie, UdeS
                values.add(new PostgresValue("ChIJywfUkEyzt0wRPYYdc8CzfbU"));
            } else {
                // TODO: do we need to call this every time? can we just fetch the db to
                // validate if we already have an associated place-id?
                try {
                    String placeData = MapsService.getPlaceDataFromAddress(location);
                    Location place = new Location(placeData);

                    if (place.isValid()) {
                        columns.add("place_id");
                        columns.add("coords");
                        columns.add("address");
                        values.add(new PostgresValue(place.getPlaceId()));
                        values.add(new PostgresValue(place.getCoords()));
                        values.add(new PostgresValue(place.getAddress()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        String[] columnArray = new String[columns.size()];
        PostgresValue[] valueArray = new PostgresValue[values.size()];
        pgh.insert_row("calendarevent", columns.toArray(columnArray), values.toArray(valueArray));
    }

    public void associateEventToUser(String eventId) {
        pgh.insert_row("attendance", new String[] {"cip", "event_id"}, new PostgresValue[] {
                new PostgresValue(cip), new PostgresValue(Integer.parseInt(eventId))});
    }

    public void registerItinerary(MissingTraject missing, String itinerary) {
        String filename = missing.getFilename();
        JsonObject itineraryJson = new JsonObject(itinerary);
        writeToFile(filename, itineraryJson.toString());

        try {
            if (itineraryJson.getString("status").toUpperCase().trim().equals("OK")) {
                JsonArray routes = itineraryJson.getJsonArray("routes");
                JsonArray legs = routes.getJsonObject(0).getJsonArray("legs");
                JsonObject leg = legs.getJsonObject(0);
                String[] columns = new String[] {"begin_time", "end_time", "transport_name",
                        "start_place_id", "end_place_Id"};

                long startTime = 0, arrivalTime = 0;
                TravelMode transport = missing.getTravelMode();
                if (transport == TravelMode.TRANSIT) {
                    startTime = leg.getJsonObject("departure_time").getLong("value") * 1000;
                    arrivalTime = leg.getJsonObject("arrival_time").getLong("value") * 1000;
                } else {
                    startTime = Utils
                            .removeTimeFromEpoch(Utils.timestampToMillis(missing.getArrivalTime()));
                    arrivalTime = startTime + leg.getJsonObject("duration").getLong("value") * 1000;
                }

                PostgresValue[] values =
                        new PostgresValue[] {new PostgresValue(new Timestamp(startTime)),
                                new PostgresValue(new Timestamp(arrivalTime)),
                                new PostgresValue(transport.toString().toUpperCase()),
                                new PostgresValue(missing.getStartPlaceId()),
                                new PostgresValue(missing.getEndPlaceId())};
                pgh.insert_row("traject", columns, values);
            } else {
                throw new Exception("Cannot go from " + missing.getStartPlaceId() + " to "
                        + missing.getEndPlaceId() + " via " + missing.getTravelMode());
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            return;
        }
    }

    private void writeToFile(String filename, String content) {
        try {
            File directory = new File("itineraries");
            if (!directory.exists()) {
                directory.mkdir();
            }

            Writer writer = new OutputStreamWriter(
                    new FileOutputStream("itineraries/" + filename + ".json"),
                    StandardCharsets.UTF_8);
            writer.write(content);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String readFile(String filename) {
        try {
            StringBuilder content = new StringBuilder();
            File file = new File("itineraries/" + filename + ".json");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                content.append(reader.nextLine());
            }
            reader.close();
            return content.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private ResultSet executeRequestForUser(String tablename) {
        try {
            String query = "SELECT * FROM " + tablename + " WHERE cip=?";
            PreparedStatement statement = pgh.getConnection().prepareStatement(query);
            statement.setString(1, cip);
            return pgh.executeQuery(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
