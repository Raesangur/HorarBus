package com.horarbus;

public class EventHandler {
    private PostgresService pgs = null;
    private int event_id = 0;

    EventHandler(int event_id) {
        this.event_id = event_id;
        pgs = new PostgresService();
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
}
