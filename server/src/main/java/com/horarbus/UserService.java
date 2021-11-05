package com.horarbus;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class UserService {
    private PostgresService pgs = null;
    private String cip = null;

    public UserService(String cip) {
        if (cip == "") {
            System.out.println("Invalid CIP: No input");
            return;
        }

        cip = cip.toLowerCase().strip();
        if (cip.length() != 8) {   // cip: abcd1234
            System.out.println("Invalid CIP: Invalid length");
            return;
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
            return;
        }

        this.cip = cip;
        pgs = new PostgresService();
    }

    public boolean is_valid() {
        return cip != null && pgs != null;
    }

    private String select_column(String column) {
        return pgs.select_column(column, "Etudiant", "cip", cip);
    }

    public String get_ical_key() {
        return select_column("cle_ical");
    }

    public int get_temps_avance_default() {
        String val = select_column("temps_avance_default");

        return Integer.parseInt(val);
    }

    public int get_temps_notification() {
        String val = select_column("temps_notification");

        return Integer.parseInt(val);
    }

    public String get_transport_default() {
        return select_column("transport_default");
    }

    public JsonObject get_preferences() {
        JsonObject response = new JsonObject();
        response.put("cip", cip);
        response.put("temps_avance_default", get_temps_avance_default());
        response.put("temps_notification_default", get_temps_notification());
        response.put("transport_default", get_transport_default());

        return response;
    }
}
