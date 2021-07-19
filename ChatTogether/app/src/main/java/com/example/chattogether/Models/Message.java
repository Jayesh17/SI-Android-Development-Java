package com.example.chattogether.Models;

public class Message {
    String message;
    String senderID;
    long time;

    public Message() {
    }

    public Message( String senderID,String message,long time) {
        this.message = message;
        this.senderID = senderID;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public long getTime() {
        return time;
    }
}
