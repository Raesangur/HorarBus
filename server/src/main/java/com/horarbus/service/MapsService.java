package com.horarbus.service;

import java.net.URLEncoder;
import com.horarbus.config.Config;
import com.horarbus.config.ConfigException;
import com.horarbus.service.http.HttpService;

public class MapsService {

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

    public static String getPlaceDataFromAddress(String address) throws Exception {
        String url = generateEndpoint("https://maps.googleapis.com/maps/api/geocode/json?address="
                + URLEncoder.encode(address, "UTF-8"));
        return MapsService.doRequest(url);
    }

    public static String getPlaceData(String placeId) {
        return "";
    }

    public static String foo() {
        try {
            return MapsService.getPlaceDataFromAddress("Faculté de génie Sherbrooke");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "An error occured when requesting maps data.";
        }
    }

}
