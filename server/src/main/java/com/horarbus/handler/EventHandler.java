package com.horarbus.handler;

import com.horarbus.Utils;
import io.vertx.core.json.JsonObject;

public class EventHandler {
    private PostgresHandler pgh = null;
    private int event_id = 0;

    EventHandler(int event_id) {
        this.event_id = event_id;
        pgh = new PostgresHandler();
    }

    private String select_column(String column) {
        return pgh.select_column(column, "Event", "event_id", event_id);
    }
    private void update_column(String column, String value) {
        pgh.update_column(column, "Event", value, "event_id", event_id);
    }
    private void update_column(String column, int value) {
        pgh.update_column(column, "Event", value, "event_id", event_id);
    }
    private void update_column(String column, long value) {
        pgh.update_column(column, "Event", value, "event_id", event_id);
    }

    private String get_start() {
        return select_column("time_start");
    }
    private void set_start(String timestamp) {
        long tmstmp = Utils.string_to_unix_milli(timestamp);
        if (tmstmp == 0) {
            return;
        }

        update_column("time_start", tmstmp);
    }

    private String get_end() {
        return select_column("time_end");
    }

    private String get_name() {
        return select_column("name");
    }

    private String get_summary() {
        return select_column("summary");
    }

    private String get_description() {
        return select_column("description");
    }

    private String get_coords() {
        return select_column("coords");
    }

    private int get_uid() {
        return Integer.parseInt(select_column("ical_id"));
    }

    public JsonObject get_event() {
        JsonObject response = new JsonObject();
        response.put("event_id", event_id);
        response.put("debut", get_start());
        response.put("fin", get_end());
        response.put("nom", get_name());
        response.put("description", get_description());
        response.put("summary", get_summary());
        response.put("coords", get_coords());
        response.put("uid", get_uid());

        return response;
    }
}
