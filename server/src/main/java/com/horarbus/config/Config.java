package com.horarbus.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Config {
    private static Config instance = null;

    private HashMap<String, String> configData;

    private Config() throws ConfigException {
        configData = new HashMap<String, String>();
        loadData();
    }

    public static String getConfig(String key) throws ConfigException {
        if (Config.instance == null) {
            Config.instance = new Config();
        }
        return Config.instance.configData.get(key);
    }

    private void loadData() throws ConfigException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("config.properties");
        if (stream != null) {
            try {
                InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] keyValue = line.split("=");
                    if (keyValue.length == 2) {
                        configData.put(keyValue[0], keyValue[1]);
                    } else {
                        throw new ConfigException("The key-value pair '" + line + "' is invalid.");
                    }
                }

                reader.close();
                streamReader.close();
            } catch (IOException ex) {
                throw new ConfigException(ex.toString());
            }
        } else {
            throw new ConfigException("Could not load config.properties.");
        }
    }
}