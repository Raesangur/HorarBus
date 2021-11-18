package com.horarbus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static long string_to_unix_milli(String timestamp){
        if(timestamp == null) {
            return 0;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (long)(epoch/1000);
        } catch(ParseException e) {
            return 0;
        }
    }

    public static String sanitizeTransport(String transport) {
        if (transport == null || transport.equals("")) {
            System.out.println("Invalid transport method");
            return null;
        }

        transport = transport.toUpperCase();

        if (transport.equals("BUS")) {
            transport = "TRANSIT";
        }

        if (transport.equals("DRIVING") || transport.equals("WALKING")
                || transport.equals("TRANSIT") || transport.equals("BICYCLING")) {
            return transport;
        } else {
            System.out.println("Invalid transport method: " + transport);
            return null;
        }
    }
}
