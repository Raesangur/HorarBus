package com.horarbus;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static long string_to_unix_milli(String timestamp) {
        if (timestamp == null) {
            return 0;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (long) (epoch / 1000);
        } catch (ParseException e) {
            return 0;
        }
    }

    public static Timestamp getTimeFromString(String str) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        // -5 -> heure normale de l'est
        return Timestamp
                .from(LocalDateTime.parse(str, formatter).toInstant(ZoneOffset.ofHours(-5)));
    }

    public static long timestampToMillis(Timestamp timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
                .withZone(ZoneId.systemDefault());
        Date d = Date.from(Instant.from(formatter.parse(timestamp.toString())));
        return d.getTime();
    }

    public static long removeTimeFromEpoch(long epoch) {
        return Date.from(new java.sql.Date(epoch).toLocalDate().atStartOfDay(ZoneId.systemDefault())
                .toInstant()).getTime();
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
