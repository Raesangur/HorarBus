package com.horarbus.handler;

public class PostgresException extends Exception {
	public PostgresException(String msg) {
		super("PostgresException: " + msg);
	}
}
