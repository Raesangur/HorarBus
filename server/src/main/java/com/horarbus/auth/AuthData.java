package com.horarbus.auth;

public class AuthData {
    private String cip;
    private String firstname;
    private String lastname;
    private String email;

    public AuthData(String cip, String firstname, String lastname, String email) {
        this.cip = cip;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "[" + cip + "]: " + firstname + " " + lastname;
    }
}
