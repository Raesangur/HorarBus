package com.horarbus.auth;

public class AuthException extends Exception {
    public AuthException(String msg) {
        super("AuthException: " + msg);
    }
}
