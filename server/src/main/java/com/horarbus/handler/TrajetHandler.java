package com.horarbus.handler;

public class TrajetHandler {
    private UserHandler user;
    private EventHandler event;

    private PostgresHandler pgh;

    public TrajetHandler(String cip, int event_id) {
        this.user = new UserHandler(cip);
        this.event = new EventHandler(event_id);

        pgh = new PostgresHandler();
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
                          new String[]{"event_id"},
                          new PostgresValue[]{new PostgresValue(event.get_id())});
    }
}
