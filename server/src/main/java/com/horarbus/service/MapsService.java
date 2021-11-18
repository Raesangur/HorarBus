package com.horarbus.service;

import java.net.URLEncoder;
import com.horarbus.config.Config;
import com.horarbus.config.ConfigException;
import com.horarbus.service.http.HttpService;

public class MapsService {

    private static final String API_BASE = "https://maps.googleapis.com/maps/api/";
    private static final String FROM_ADDRESS_URL = API_BASE + "geocode/json?address=";
    private static final String FROM_PLACE_ID_URL = API_BASE + "place/details/json?place_id=";

    public static String getPlaceDataFromAddress(String address) throws Exception {
        String endpointStr = MapsService.FROM_ADDRESS_URL + URLEncoder.encode(address, "UTF-8");
        return MapsService.doRequest(generateEndpoint(endpointStr));
    }

    public static String getPlaceDataFromId(String placeId) throws Exception {
        String endpointStr = MapsService.FROM_PLACE_ID_URL + URLEncoder.encode(placeId, "UTF-8");
        return MapsService.doRequest(generateEndpoint(endpointStr));
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
