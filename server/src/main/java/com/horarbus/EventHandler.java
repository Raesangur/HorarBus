package com.horarbus;

import io.vertx.core.json.JsonObject;

public class EventHandler {
    private PostgresHandler pgs = null;
    private int event_id = 0;

    EventHandler(int event_id) {
        this.event_id = event_id;
        pgs = new PostgresHandler();
    }

    private String select_column(String column) {
        return pgs.select_column(column, "Evenement", "event_id", event_id);
    }

    private String get_debut() {
        return select_column("debut_event");
    }

    private String get_fin() {
        return select_column("fin_event");
    }

    private String get_nom() {
        return select_column("nom_event");
    }

    private String get_summary() {
        return select_column("note");
    }

    private String get_description() {
        return select_column("description");
    }

    private String get_coords() {
        return select_column("coordonnees");
    }

    private int get_uid() {
        return Integer.parseInt(select_column("uid"));
    }

    public JsonObject get_event() {
        JsonObject response = new JsonObject();
        response.put("event_id", event_id);
        response.put("debut", get_debut());
        response.put("fin", get_fin());
        response.put("nom", get_nom());
        response.put("description", get_description());
        response.put("summary", get_summary());
        response.put("coord", get_coords());
        response.put("uid", get_uid());

        return response;
    }
}
