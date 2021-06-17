package com.example.mediaapp;

public class MP3File {

    private String name;
    private String path;

    public MP3File() {
    }

    public MP3File(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

}
