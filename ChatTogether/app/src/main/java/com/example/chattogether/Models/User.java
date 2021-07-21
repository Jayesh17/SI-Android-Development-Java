package com.example.chattogether.Models;

public class User {

    String name;
    String email;
    String phone;
    String status;
    String profileUri;
    String UID;

    public User() {
    }

    public User(String name, String email, String phone, String status, String profileUri, String UID) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.profileUri = profileUri;
        this.UID = UID;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
