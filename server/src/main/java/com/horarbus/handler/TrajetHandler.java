package com.horarbus.handler;

public class TrajetHandler {
    private UserHandler user;
    private EventHandler event;

    private PostgresHandler pgh;

    public TrajetHandler(String cip, int event_id) {
        this.user = new UserHandler(cip);
        this.event = new EventHandler(event_id);

        pgh = new PostgresHandler();

        pgh.insert_row("Traject", new String[] {"cip", "event_id"},
                       new PostgresValue[]{new PostgresValue(user.get_cip()),
                                           new PostgresValue(event.get_event_id)});
    }

    private String select_column(String column) {
        return pgh.select_column(column, "Traject",
                                 new String[]{"cip", "event_id"},
                                 new PostgresValue[]{new PostgresValue(user.get_cip()),
                                                     new PostgresValue(event.get_id())});
    }
    private void update_column(String column, String value) {
        pgh.update_column(column, "Event",
                          new PostgresValue(value),
                          new String[]{"cip, event_id"},
                          new PostgresValue[]{new PostgresValue(user.get_cip()),
                                              new PostgresValue(event.get_id())});
    }

    public UserHandler get_user() {
        return user;
    }
    public EventHandler get_event() {
        return event;
    }
    public String get_cip() {
        return user.get_cip();
    }
    public int get_event_id() {
        return event.get_event_id();
    }

    public String get_start_coords() {
        return select_column("coords_start");
    }

    public LocationHandler get_start_loc() {

    }

    public 
}
