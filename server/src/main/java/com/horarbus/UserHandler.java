package com.horarbus;

import io.vertx.core.json.JsonObject;

public class UserHandler {
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
        return pgh.select_column(column, "Etudiant", "cip", cip);
    }
    private void update_column(String column, String value) {
        pgh.update_column(column, "Student", value, "cip", cip);
    }
    private void update_column(String column, int value) {
        pgh.update_column(column, "Student", value, "cip", cip);
    }

    public String get_ical_key() {
        return select_column("cle_ical");
    }
    public void set_ical_key(String ical_key) {
        update_column("ical_key", ical_key);
    }

    public int get_preparation_time() {
        String val = select_column("preparation_time");

        return Integer.parseInt(val);
    }
    public void set_preparation_time(int prep_time) {
        update_column("preparation_time", prep_time);
    }

    public int get_notification_time() {
        String val = select_column("notification_time");

        return Integer.parseInt(val);
    }
    public void set_notification_time(int notif_time) {
        update_column("notification_time", notif_time);
    }

    private String sanitizeTransport(String transport){
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
        return select_column("transport");
    }
    public void set_transport(String transport) {
        transport = sanitizeTransport(transport);
        if (transport == null) {
            return;
        }

        update_column("transport", transport);
    }

    public JsonObject get_preferences() {
        JsonObject response = new JsonObject();
        response.put("cip", cip);
        response.put("preparation_time", get_preparation_time());
        response.put("notification_time", get_notification_time());
        response.put("transport", get_transport());

        return response;
    }
}
