package com.horarbus.auth;

public class AuthData {
    private String cip;
    private String firstname;
    private String lastname;

    public AuthData(String cip, String firstname, String lastname) {
        this.cip = cip;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getCip() {
        return cip;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return "[" + cip + "]: " + firstname + " " + lastname;
    }
}
