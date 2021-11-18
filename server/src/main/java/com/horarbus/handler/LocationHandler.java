package com.horarbus.handler;

public class LocationHandler {
    private String place_id;
    private PostgresHandler pgh;

    public LocationHandler(String place_id) {
        this.place_id = place_id;

        pgh = new PostgresHandler();

        if (select_column("place_id") == "") {
            pgh.insert_row("Location",
                           new String[]{"place_id"},
                           new PostgresValue[]{new PostgresValue(place_id)});
        }
    }

    private String select_column(String column) {
        return pgh.select_column(column, "Location",
                                 new String[]{"place_id"},
                                 new PostgresValue[]{new PostgresValue(place_id)});
    }
    private void update_column(String column, String value) {
        pgh.update_column(column, "Location",
                          new PostgresValue(value),
                          new String[]{"place_id"},
                          new PostgresValue[]{new PostgresValue(place_id)});
    }

    public String get_id() {
        return place_id;
    }
}
