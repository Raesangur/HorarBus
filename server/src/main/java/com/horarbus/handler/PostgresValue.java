package com.horarbus.handler;

import java.sql.Timestamp;

public class PostgresValue {
    public enum PostgresValueType {
        integer, string, timestamp, unknown
    }

    private Integer valueInt = null;
    private String valueString = null;
    private Timestamp valueTimestamp = null;

    public PostgresValue(String value) {
        this.valueString = value;
    }

    public PostgresValue(int value) {
        valueInt = value;
    }

    public PostgresValue(Timestamp value) {
        valueTimestamp = value;
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

    public Timestamp getTimestamp() {
        if (getType() == PostgresValueType.timestamp) {
            return valueTimestamp;
        } else {
            return null;
        }
    }

    public PostgresValueType getType() {
        if (valueString != null && valueInt == null && valueTimestamp == null) {
            return PostgresValueType.string;
        } else if (valueInt != null && valueString == null && valueTimestamp == null) {
            return PostgresValueType.integer;
        } else if (valueTimestamp != null && valueInt == null && valueString == null) {
            return PostgresValueType.timestamp;
        } else {
            return PostgresValueType.unknown;
        }
    }
}
