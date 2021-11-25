package com.horarbus.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.horarbus.Utils;
import com.horarbus.service.CalendarService;
import com.horarbus.service.MapsService;

import biweekly.component.VEvent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class CalendarHandler {
    private PostgresHandler pgh = null;

    public CalendarHandler() {
        pgh = new PostgresHandler();
    }

    public JsonArray getAllEvents() {
        String query = "SELECT * FROM calendarevent";
        ResultSet results = pgh.executeQuery(query);

        try{
            JsonArray events = new JsonArray();
            while(results.next()){
                VEvent event  = new VEvent();
                event.setDescription(results.getString("description"));
                event.setSummary(results.getString("summary"));
                event.setDateStart(results.getTimestamp("start_time"), true);
                event.setDateEnd(results.getTimestamp("end_time"),true);
                event.setColor(results.getString("color"));
                event.setLocation(results.getString("local"));
                events.add(CalendarService.parseEvent(event));
            }
            return events;
        }catch(SQLException ex){
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
        values.add(
                new PostgresValue(Utils.getTimeFromString(eventData.getDateEnd().getValue().toString())));
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
            if(pattern.matcher(location).matches()){
                columns.add("local");
                values.add(new PostgresValue(location));
                columns.add("place_id");
                // Faculté de Génie, UdeS
                values.add(new PostgresValue("ChIJywfUkEyzt0wRPYYdc8CzfbU"));
            }else{
                // TODO: do we need to call this every time? can we just fetch the db to validate if we already have an associated place-id?
                try{
                    String placeData = MapsService.getPlaceDataFromAddress(location);
                    JsonObject json = new JsonObject(placeData);
                    JsonArray jsonArr = json.getJsonArray("results");
                    if(jsonArr!=null && jsonArr.size() > 0){
                        JsonObject  result = jsonArr.getJsonObject(0);
                        String address = result.getString("formatted_address");
                        String placeId = result.getString("place_id");
                        JsonObject locationObj = result.getJsonObject("geometry").getJsonObject("location");
                        String coords = locationObj.getString("lat")+","+locationObj.getString("lng");
                    
                        columns.add("place_id");
                        columns.add("coords");
                        columns.add("address");
                        values.add(new PostgresValue(placeId));
                        values.add(new PostgresValue(coords));
                        values.add(new PostgresValue(address));
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        String[] columnArray = new String[columns.size()];
        PostgresValue[] valueArray = new PostgresValue[values.size()];
        pgh.insert_row("calendarevent", columns.toArray(columnArray), values.toArray(valueArray));
    }

    public void associateEventToUser(String eventId, String cip){
        pgh.insert_row("attendance", new String[] {"cip","event_id"}, new PostgresValue[]{new PostgresValue(cip),new PostgresValue(Integer.parseInt(eventId))});
    }
}
