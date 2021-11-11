package com.horarbus.handler;

public class PostgresValue {
    public enum PostgresValueType {
        integer,
        string,
        timestamp,
        unknown
    }

    private Integer valueInt = null;
    private String valueString = null;
    private Long valueLong = null;

    public PostgresValue(String value) {
        this.valueString = value;
    }
    public PostgresValue(int value) {
        valueInt = value;
    }
    public PostgresValue(long value) {
        valueLong = value;
    }

    public int getInt() {
        if (getType() == PostgresValueType.integer) {
            return valueInt;
        } else {
            return 0;
        }
    }

    public String getString() {
        if (getType() == PostgresValueType.string) {
            return valueString;
        } else {
            return null;
        }
    }

    public Long getTimestamp() {
        if (getType() == PostgresValueType.timestamp) {
            return valueLong;
        } else {
            return null;
        }
    }

    public PostgresValueType getType() {
        if (valueString != null && valueInt == null && valueLong == null) {
            return PostgresValueType.string;
        } else if(valueInt != null && valueString == null && valueLong == null) {
            return PostgresValueType.integer;
        } else if(valueLong != null && valueInt == null && valueString == null) {
            return PostgresValueType.timestamp;
        } else {
            return PostgresValueType.unknown;
        }
    }
}
