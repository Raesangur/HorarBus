package com.horarbus.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationHandler {
    private PostgresHandler pgh;

    private String place_id;
    private String coords;
    private String address;

    public LocationHandler(String place_id) {
        this.place_id = place_id;

        pgh = new PostgresHandler();
        coords = null;
        address = null;

        try {
            ResultSet results = pgh.executeQuery(
                    "SELECT * FROM localisation WHERE UPPER(place_id) = UPPER('" + place_id + "')");
            if (results != null) {
                while (results.next()) {
                    place_id = results.getString("place_id");
                    coords = results.getString("coords");
                    address = results.getString("address");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String get_id() {
        return place_id;
    }

    public String get_coords() {
        return coords;
    }

    public String get_address() {
        return address;
    }
}
