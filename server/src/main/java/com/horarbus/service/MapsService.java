package com.horarbus.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.google.maps.model.TravelMode;
import com.horarbus.config.Config;
import com.horarbus.config.ConfigException;
import com.horarbus.service.http.HttpService;

public class MapsService {

    private static final String API_BASE = "https://maps.googleapis.com/maps/api/";
    private static final String FROM_ADDRESS_URL = API_BASE + "geocode/json?address=";
    private static final String FROM_PLACE_ID_URL = API_BASE + "place/details/json?place_id=";
    private static final String ITINERARY_URL = API_BASE + "directions/json?";

    public static String getPlaceDataFromAddress(String address) throws Exception {
        String endpointStr = MapsService.FROM_ADDRESS_URL + formatValue(address);
        return MapsService.doRequest(generateEndpoint(endpointStr));
    }

    public static String getPlaceDataFromId(String placeId) throws Exception {
        String endpointStr = MapsService.FROM_PLACE_ID_URL + formatValue(placeId);
        return MapsService.doRequest(generateEndpoint(endpointStr));
    }

    public static String getItineraryWithArrival(String startingPlaceId, String arrivalPlaceId,
            TravelMode travelMode, long arrivalTime) throws Exception {
        return MapsService.getItinerary(startingPlaceId, arrivalPlaceId, travelMode, arrivalTime,
                "arrival_time");
    }

    public static String getItineraryWithDeparture(String startingPlaceId, String arrivalPlaceId,
            TravelMode travelMode, long arrivalTime) throws Exception {
        return MapsService.getItinerary(startingPlaceId, arrivalPlaceId, travelMode, arrivalTime,
                "departure_time");
    }

    private static String getItinerary(String startingPlaceId, String arrivalPlaceId,
            TravelMode travelMode, long arrivalTime, String timeSpecification) throws Exception {
        String params = "origin=place_id:" + formatValue(startingPlaceId) + "&destination=place_id:"
                + formatValue(arrivalPlaceId) + "&mode=" + travelMode.toUrlValue();

        if (travelMode == TravelMode.TRANSIT) {
            params += "&" + timeSpecification + "=" + Long.toString(arrivalTime);
        }

        String endpointStr = MapsService.ITINERARY_URL + params;
        // System.out.println(endpointStr);
        return MapsService.doRequest(generateEndpoint(endpointStr));
    }

    private static String formatValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    private static String ApiKey() {
        try {
            return Config.getConfig("googleApiKey");
        } catch (ConfigException ex) {
            return null;
        }
    }

    private static String generateEndpoint(String url) {
        return url + "&key=" + MapsService.ApiKey();
    }

    private static String doRequest(String endpointUrl) throws Exception {
        HttpService service = new HttpService();
        service.setRequestMethod("GET");
        service.setURL(endpointUrl);
        return service.executeRequest();
    }

}
