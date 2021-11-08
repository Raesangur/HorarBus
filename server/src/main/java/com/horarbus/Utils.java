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
}
