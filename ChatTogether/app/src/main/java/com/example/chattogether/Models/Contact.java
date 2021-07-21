package com.example.chattogether.Models;

public class Contact {

    String name;
    String phone;
    String userID;


    public Contact() {
    }

    public Contact(String name, String phone, String userID) {
        this.name = name;
        this.phone = phone;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
