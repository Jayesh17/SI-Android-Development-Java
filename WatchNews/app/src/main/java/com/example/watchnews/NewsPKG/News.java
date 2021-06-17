package com.example.watchnews.NewsPKG;

public class News {

    private String title;
    private String URL;
    private String publishTime;

    public News(String title,String URL,String publishTime)
    {
        this.title = title;
        this.URL = URL;
        this.publishTime= publishTime;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }

    public String getPublishTime() {
        return publishTime;
    }
}
