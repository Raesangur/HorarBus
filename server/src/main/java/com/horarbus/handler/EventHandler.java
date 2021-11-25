package com.horarbus.handler;

import java.lang.reflect.Array;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import org.postgresql.jdbc.TimestampUtils;
import biweekly.component.VEvent;
import biweekly.property.Timezone;

public class EventHandler {
    private PostgresHandler pgh = null;
    private VEvent eventData = null;

    public EventHandler(VEvent event) {
        this.eventData = event;
        pgh = new PostgresHandler();
    }

    public void cacheData() {
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("event_id");
        columns.add("start_time");
        columns.add("end_time");
        columns.add("description");
        columns.add("summary");

        ArrayList<PostgresValue> values = new ArrayList<PostgresValue>();
        values.add(new PostgresValue(Integer.parseInt(eventData.getUid().getValue())));
        values.add(new PostgresValue(
                getTimeFromString(eventData.getDateStart().getValue().toString())));
        values.add(
                new PostgresValue(getTimeFromString(eventData.getDateEnd().getValue().toString())));
        values.add(new PostgresValue(eventData.getDescription().getValue().toString()));
        values.add(new PostgresValue(eventData.getSummary().getValue().toString()));

        if (eventData.getColor() != null) {
            columns.add("color");
            values.add(new PostgresValue(eventData.getColor().getValue().toString()));
        }

        if (eventData.getLocation() != null) {
            // TODO
        }

        String[] columnArray = new String[columns.size()];
        PostgresValue[] valueArray = new PostgresValue[values.size()];
        pgh.insert_row("calendarevent", columns.toArray(columnArray), values.toArray(valueArray));
    }

    private Timestamp getTimeFromString(String str) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        // -5 -> heure normale de l'est
        return Timestamp
                .from(LocalDateTime.parse(str, formatter).toInstant(ZoneOffset.ofHours(-5)));
    }

    // private String select_column(String column) {
    // String[] result = pgh.select_column(column, "Event", new String[] {"event_id"},
    // new PostgresValue[] {event_id});
    // return result == null ? "" : result[0];
    // }

    // private void update_column(String column, String value) {
    // pgh.update_column(column, "Event", new PostgresValue(value), new String[] {"event_id"},
    // new PostgresValue[] {event_id});
    // }

    // private void update_column(String column, int value) {
    // pgh.update_column(column, "Event", new PostgresValue(value), new String[] {"event_id"},
    // new PostgresValue[] {event_id});
    // }

    // private void update_column(String column, long value) {
    // pgh.update_column(column, "Event", new PostgresValue(value), new String[] {"event_id"},
    // new PostgresValue[] {event_id});
    // }

    // public int get_id() {
    // return event_id.getInt();
    // }

    // public String get_start() {
    // return select_column("time_start");
    // }

    // public void set_start(String timestamp) {
    // long tmstmp = Utils.string_to_unix_milli(timestamp);
    // if (tmstmp == 0) {
    // return;
    // }

    // update_column("time_start", tmstmp);
    // }

    // public String get_end() {
    // return select_column("time_end");
    // }

    // public String get_name() {
    // return select_column("name");
    // }

    // public String get_summary() {
    // return select_column("summary");
    // }

    // public String get_description() {
    // return select_column("description");
    // }

    // public String get_coords() {
    // return select_column("coords");
    // }

    // public int get_uid() {
    // return Integer.parseInt(select_column("ical_id"));
    // }

    // public JsonObject get_event() {
    // JsonObject response = new JsonObject();
    // response.put("event_id", event_id);
    // response.put("debut", get_start());
    // response.put("fin", get_end());
    // response.put("nom", get_name());
    // response.put("description", get_description());
    // response.put("summary", get_summary());
    // response.put("coords", get_coords());
    // response.put("uid", get_uid());

    // return response;
    // }
}
