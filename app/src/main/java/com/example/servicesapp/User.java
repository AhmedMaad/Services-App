package com.example.servicesapp;

public class User {

    private String email;
    private String userType;
    private String picture;
    private String name;
    private String about;
    private String phone;
    private String hourlyRate;
    private String location;
    private double lat;
    private double lon;


    //Used for sign up
    public User(String email, String userType) {
        this.email = email;
        this.userType = userType;
    }

    public User(String email, String userType, String picture, String name, String about
            , String phone, String hourlyRate, String location, double lat, double lon) {
        this.email = email;
        this.userType = userType;
        this.picture = picture;
        this.name = name;
        this.about = about;
        this.phone = phone;
        this.hourlyRate = hourlyRate;
        this.location = location;
        this.lat = lat;
        this.lon = lon;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getPhone() {
        return phone;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public String getLocation() {
        return location;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
