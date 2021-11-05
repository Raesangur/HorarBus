package com.horarbus;

public class UserService {
    private PostgresService pgs = null;
    private String cip = null;

    public UserService(String cip) {
        this.cip = cip;
        pgs = new PostgresService();
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
}
