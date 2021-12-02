package com.horarbus;

import com.horarbus.handler.UserHandler;
import io.vertx.core.json.JsonObject;

public class UserPrefs {
    private int preparationTime;
    private int notificationTime;
    private String transport;
    private String localAddress;
    private String icalKey;

    private boolean notificationEnabled;
    private boolean isDarkMode;

    public UserPrefs(UserHandler handler) {
        preparationTime = handler.get_preparation_time();
        notificationTime = handler.get_notification_time();
        transport = handler.get_transport();
        localAddress = handler.get_default_address();

        notificationEnabled = handler.get_notification_enable();
        isDarkMode = handler.get_darkmode();
    }

    public UserPrefs(JsonObject json) throws Exception {
        try {
            JsonObject notificationJson = json.getJsonObject("notification");

            preparationTime = json.getInteger("preparation_time");
            notificationTime = notificationJson.getInteger("time");
            notificationEnabled = notificationJson.getBoolean("enabled");
            icalKey = json.getString("Ical");
            transport = json.getString("transport");
            localAddress = json.getString("local_address");
            isDarkMode = json.getBoolean("dark_mode");
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void saveData(UserHandler handler) {
        handler.set_preparation_time(preparationTime);
        handler.set_notification_time(notificationTime);
        handler.set_transport(transport);
        handler.set_darkmode(isDarkMode);
        handler.set_notification_enable(notificationEnabled);
        handler.set_default_address(localAddress);
        if (icalKey != null) {
            handler.set_ical_key(icalKey);
        }
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        JsonObject notificationJson = new JsonObject();
        notificationJson.put("time", notificationTime);
        notificationJson.put("enabled", notificationEnabled);

        json.put("preparation_time", preparationTime);
        json.put("transport", transport);
        json.put("local_address", localAddress);
        json.put("notification", notificationJson);
        json.put("dark_mode", isDarkMode);

        return json;
    }
}
