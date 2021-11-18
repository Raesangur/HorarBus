package com.horarbus.handler;

public class LocationHandler {
    private String place_id;
    private PostgresHandler pgh;

    LocationHandler(String place_id) {
        this.place_id = place_id;
        PostgresHandler pgh = new PostgresHandler();
    }
}
