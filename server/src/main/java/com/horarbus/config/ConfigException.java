package com.horarbus.config;

public class ConfigException extends Exception {

    public ConfigException(String msg) {
        super("ConfigException: " + msg);
    }
}
