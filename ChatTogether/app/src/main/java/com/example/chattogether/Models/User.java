package com.example.chattogether.Models;

public class User {

    String name;
    String email;
    String status;
    String profileUri;
    String UID;

    public User() {
    }

    public User(String name, String email, String profileUri,String status, String UID) {
        this.name = name;
        this.email = email;
        this.profileUri = profileUri;
        this.status = status;
        this.UID = UID;
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
