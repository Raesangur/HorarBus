package com.horarbus.handler;

import com.horarbus.Utils;

public class TrajetHandler {
    private UserHandler user;
    private EventHandler event;

    private PostgresHandler pgh;

    public TrajetHandler(String cip, int event_id) {
        this.user = new UserHandler(cip);
        this.event = new EventHandler(event_id);
        
        pgh = new PostgresHandler();
        if (select_column("cip") == "") {
            Thread.dumpStack();
        }
    }

    public TrajetHandler(UserHandler user, EventHandler event, LocationHandler start, LocationHandler end, long arrival_time) {
        this.user = user;
        this.event = event;

        pgh = new PostgresHandler();
        pgh.insert_row("Traject", new String[] {"cip", "event_id", "coords_start", "coords_end", "arrival_time"},
                       new PostgresValue[]{new PostgresValue(user.get_cip()),
                                           new PostgresValue(event.get_id()),
                                           new PostgresValue(start.get_id()),
                                           new PostgresValue(end.get_id()),
                                           new PostgresValue(arrival_time)});
    }

    private String select_column(String column) {
        return pgh.select_column(column, "Traject",
                                 new String[]{"cip", "event_id"},
                                 new PostgresValue[]{new PostgresValue(user.get_cip()),
                                                     new PostgresValue(event.get_id())});
    }
    private void update_column(String column, String value) {
        pgh.update_column(column, "Traject",
                          new PostgresValue(value),
                          new String[]{"cip", "event_id"},
                          new PostgresValue[]{new PostgresValue(user.get_cip()),
                                              new PostgresValue(event.get_id())});
    }
    private void update_column(String column, long value) {
        pgh.update_column(column, "Traject",
                          new PostgresValue(value),
                          new String[]{"cip", "event_id"},
                          new PostgresValue[]{new PostgresValue(user.get_cip()),
                                              new PostgresValue(event.get_id())});
    }

    public UserHandler get_user() {
        return user;
    }
    public String get_cip() {
        return user.get_cip();
    }

    public EventHandler get_event() {
        return event;
    }
    public int get_event_id() {
        return event.get_id();
    }

    public LocationHandler get_start() {
        return new LocationHandler(get_coords_start());
    }
    public String get_coords_start() {
        return select_column("coords_start");
    }

    public LocationHandler get_end() {
        return new LocationHandler(get_coords_end());
    }
    public String get_coords_end() {
        return select_column("coords_end");
    }
    public void set_coords_end(LocationHandler coords) {
        update_column("coords_end", coords.get_id());
    }

    private String get_arrival_time() {
        return select_column("arrival_time");
    }
    private void set_arrival_time(String timestamp) {
        long tmstmp = Utils.string_to_unix_milli(timestamp);
        if (tmstmp == 0) {
            return;
        }

        update_column("arrival_time", tmstmp);
    }

    public String get_transport() {
        String transport = select_column("transport");
        return transport != null ? transport : user.get_transport();
    }
    public void set_transport(String transport) {
        transport = Utils.sanitizeTransport(transport);
        if (transport == null) {
            transport = user.get_transport();
            if (transport == null) {
                return;
            }
        }

        update_column("transport", transport);
    }

    private String get_begin_time() {
        return select_column("begin_time");
    }
    private void set_begin_time(String timestamp) {
        long tmstmp = Utils.string_to_unix_milli(timestamp);
        if (tmstmp == 0) {
            return;
        }

        update_column("begin_time", tmstmp);
    }

    private int get_preparation_time() {
        return Integer.parseInt(select_column("preparation_time"));
    }
    private void set_preparation_time(int prep) {
        prep = prep >= 0 ? prep : -prep;
        update_column("preparation_time", prep);
    }
}
