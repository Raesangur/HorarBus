package com.horarbus.resource;

import javax.validation.TraversableResolver;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.maps.model.TravelMode;
import com.horarbus.service.MapsService;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@Path("/itinerary")
public class ItineraryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public String getUserData(JsonObject body) {
        String departureAddress = null, arrivalAddress = null, transportMode = null;
        int targetTime = 0;

        try {
            departureAddress = body.getString("departure");
            arrivalAddress = body.getString("arrival");
            transportMode = body.getString("mode");
            targetTime = body.getInteger("target_time");
        } catch (Exception ex) {
            ex.printStackTrace();
            JsonObject errorJson = new JsonObject();
            errorJson.put("error", "Invalid or missing parameter in body.");
            return errorJson.toString();
        }

        try {
            JsonObject departurePlaceData =
                    new JsonObject(MapsService.getPlaceDataFromAddress(departureAddress));
            JsonObject arrivalPlaceData =
                    new JsonObject(MapsService.getPlaceDataFromAddress(arrivalAddress));

            String departurePlaceId = drillToPlaceId(departurePlaceData);
            String arrivalPlaceId = drillToPlaceId(arrivalPlaceData);
            TravelMode travelMode = stringToTravelMode(transportMode);

            System.out.println(departurePlaceId + " " + arrivalPlaceData + " " + travelMode + " "
                    + targetTime);

            return MapsService.getItinerary(departurePlaceId, arrivalPlaceId, travelMode,
                    targetTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            JsonObject errorObj = new JsonObject();
            errorObj.put("error", "Something did not work lolz");
            return errorObj.toString();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/place")
    public String getPlaceInfo(JsonObject body) {
        String placeId = null;

        try {
            placeId = body.getString("place_id");
        } catch (Exception ex) {
            ex.printStackTrace();
            JsonObject errorJson = new JsonObject();
            errorJson.put("error", "Invalid or missing parameter in body.");
            return errorJson.toString();
        }

        try {
            return MapsService.getPlaceDataFromId(placeId);
        } catch (Exception ex) {
            ex.printStackTrace();
            JsonObject errorObj = new JsonObject();
            errorObj.put("error", "Something did not work lolz");
            return errorObj.toString();
        }
    }

    private TravelMode stringToTravelMode(String travelModeStr) {
        switch (travelModeStr.toUpperCase().trim()) {
            case "DRIVING":
                return TravelMode.DRIVING;
            case "WALKING":
                return TravelMode.WALKING;
            case "BICYCLING":
                return TravelMode.BICYCLING;
            case "TRANSIT":
                return TravelMode.TRANSIT;
            default:
                return TravelMode.UNKNOWN;
        }
    }

    private String drillToPlaceId(JsonObject placeData) throws Exception {
        JsonArray results = placeData.getJsonArray("results");
        if (results.size() >= 1) {
            JsonObject firstResult = results.getJsonObject(0);
            return firstResult.getString("place_id");
        }
        return null;
    }

}
