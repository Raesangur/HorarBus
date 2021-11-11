package com.horarbus;

public class UserHandler {
    private static final int DEFAULT_PREPARATION_TIME = 15;
    private static final int DEFAULT_NOTIFICATION_TIME = 15;
    private static final String DEFAULT_TRANSPORT = "TRANSIT";

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

        pgh.insert_row("etudiant",
                        new String[]{"cip", "nom", "prenom"},
                        new String[]{cip, nom, prenom});
    }

    private boolean validate_cip(String cip) {
        if (cip == "") {
            System.out.println("Invalid CIP: No input");
            return false;
        }

        cip = cip.toLowerCase().strip();
        if (cip.length() != 8) {   // cip: abcd1234
            System.out.println("Invalid CIP: Invalid length");
             return false;
        }

        if (!Character.isLetter(cip.charAt(0)) ||
                !Character.isLetter(cip.charAt(1)) ||
                !Character.isLetter(cip.charAt(2)) ||
                !Character.isLetter(cip.charAt(3)) ||
                !Character.isDigit(cip.charAt(4))  ||
                !Character.isDigit(cip.charAt(5))  ||
                !Character.isDigit(cip.charAt(6))  ||
                !Character.isDigit(cip.charAt(7))) {
            System.out.println("Invalid CIP: Invalid format");
            return false;
        }

        return true;
    }

    public boolean is_valid() {
        return cip != null && pgh != null;
    }

    private String select_column(String column) {
        return pgh.select_column(column, "Student", "cip", cip);
    }

    private void update_column(String column, String value) {
        pgh.update_column(column, "Student", value, "cip", cip);
    }

    private void update_column(String column, int value) {
        pgh.update_column(column, "Student", value, "cip", cip);
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

        if (transport.equals("DRIVING") ||
            transport.equals("WALKING") ||
            transport.equals("TRANSIT") ||
            transport.equals("BICYCLING")) {
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
}
