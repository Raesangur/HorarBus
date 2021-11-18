package com.horarbus.handler;

import com.horarbus.Utils;

public class UserHandler {
    private static final int DEFAULT_PREPARATION_TIME = 15;
    private static final int DEFAULT_NOTIFICATION_TIME = 15;
    private static final String DEFAULT_TRANSPORT = "TRANSIT";
    private static String DEFAULT_ADDRESS = "Université de Sherbrooke";

    private PostgresHandler pgh = null;
    private String cip = null;

    public UserHandler(String cip) {
        if (!validate_cip(cip)) {
            return;
        }

        this.cip = cip;
        pgh = new PostgresHandler();
    }

    public UserHandler(String cip, String nom, String prenom) {
        if (!validate_cip(cip)) {
            return;
        }

        this.cip = cip;
        pgh = new PostgresHandler();

        if (select_column("cip") == "") {
            pgh.insert_row("Student", new String[] {"cip", "name", "surname"},
                        new PostgresValue[]{new PostgresValue(cip),
                                            new PostgresValue(nom),
                                            new PostgresValue(prenom)});
        }
    }

    private boolean validate_cip(String cip) {
        if (cip == "") {
            System.out.println("Invalid CIP: No input");
            return false;
        }

        cip = cip.toLowerCase().strip();
        if (cip.length() != 8) { // cip: abcd1234
            System.out.println("Invalid CIP: Invalid length");
            return false;
        }

        if (!Character.isLetter(cip.charAt(0)) || !Character.isLetter(cip.charAt(1))
                || !Character.isLetter(cip.charAt(2)) || !Character.isLetter(cip.charAt(3))
                || !Character.isDigit(cip.charAt(4)) || !Character.isDigit(cip.charAt(5))
                || !Character.isDigit(cip.charAt(6)) || !Character.isDigit(cip.charAt(7))) {
            System.out.println("Invalid CIP: Invalid format");
            return false;
        }

        return true;
    }

    public boolean is_valid() {
        return cip != null && pgh != null;
    }

    private String select_column(String column) {
        String[] result = pgh.select_column(column, "Student",
                                            new String[]{"cip"},
                                            new PostgresValue[]{new PostgresValue(cip)});
        return result == null ? "" : result[0];
    }

    private void update_column(String column, String value) {
        pgh.update_column(column, "Student", new PostgresValue(value),
                          new String[]{"cip"},
                          new PostgresValue[]{new PostgresValue(cip)});
    }

    private void update_column(String column, int value) {
        pgh.update_column(column, "Student", new PostgresValue(value),
                          new String[]{"cip"},
                          new PostgresValue[]{new PostgresValue(cip)});
    }

    public String get_cip() {
        return cip;
    }

    public EventHandler[] get_events() {
        String[] event_ids = pgh.select_column("event_id", "Attendance",
                                               new String[]{"cip"},
                                               new PostgresValue[]{new PostgresValue(cip)});
        
        EventHandler[] events = new EventHandler[event_ids.length];
        for (int i = 0; i < event_ids.length; i++) {
            events[i] = new EventHandler(Integer.parseInt(event_ids[i]));
        }
        return events;
    }

    public String get_ical_key() {
        String key = select_column("ical_key");
        return key != null ? key : "";
    }

    public void set_ical_key(String ical_key) {
        update_column("ical_key", ical_key);
    }

    public int get_preparation_time() {
        String val = select_column("preparation_time");

        return val != null ? Integer.parseInt(val) : DEFAULT_PREPARATION_TIME;
    }

    public void set_preparation_time(int prep_time) {
        update_column("preparation_time", prep_time);
    }

    public int get_notification_time() {
        String val = select_column("notification_time");

        return val != null ? Integer.parseInt(val) : DEFAULT_NOTIFICATION_TIME;
    }

    public void set_notification_time(int notif_time) {
        update_column("notification_time", notif_time);
    }

    private String sanitizeTransport(String transport) {
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

    public String get_transport() {
        String transport = select_column("transport");
        return transport != null ? transport : DEFAULT_TRANSPORT;
    }

    public void set_transport(String transport) {
        transport = sanitizeTransport(transport);
        if (transport == null) {
            return;
        }

        update_column("transport", transport);
    }

    public boolean get_darkmode() {
        String darkModeStr = select_column("dark_mode");
        if(darkModeStr == null){
            darkModeStr = "false";
        }
        return Boolean.parseBoolean(darkModeStr.trim());
    }

    public void set_darkmode(boolean dark_mode) {
        update_column("dark_mode", Boolean.toString(dark_mode));
    }

    public boolean get_notification_enable() {
        return Boolean.parseBoolean(select_column("notification_enable"));
    }

    public void set_notification_enable(boolean notif) {
        update_column("notification_enable", Boolean.toString(notif));
    }

    public String get_default_address() {
        // TODO
        return DEFAULT_ADDRESS;
    }

    public void set_default_address(String address) {
        // TODO
    }
}
