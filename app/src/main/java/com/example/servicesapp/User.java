package com.example.servicesapp;

public class User {

    private String email;
    private String userType;

    public User(String email, String userType) {
        this.email = email;
        this.userType = userType;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

}
