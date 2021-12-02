package com.horarbus;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Location {

    private String placeId;
    private String coords;
    private String address;

    public Location(String googlePlaceData) {
        parseFromPlaceData(googlePlaceData);
    }

    private void parseFromPlaceData(String placeData) {
        JsonObject json = new JsonObject(placeData);
        JsonArray jsonArr = json.getJsonArray("results");
        if (jsonArr != null && jsonArr.size() > 0) {
            JsonObject result = jsonArr.getJsonObject(0);
            address = result.getString("formatted_address");
            placeId = result.getString("place_id");

            JsonObject locationObj = result.getJsonObject("geometry").getJsonObject("location");
            coords = locationObj.getString("lat") + "," + locationObj.getString("lng");
        } else {
            address = null;
            placeId = null;
            coords = null;
        }
    }

    public boolean isValid() {
        return getPlaceId() != null && !getPlaceId().isEmpty() && getCoords() != null
                && !getCoords().isEmpty() && getAddress() != null && !getAddress().isEmpty();
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getCoords() {
        return coords;
    }

    public String getAddress() {
        return address;
    }

}
