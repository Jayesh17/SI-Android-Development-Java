package com.example.watchnews.NewsPKG;

import java.util.HashMap;

public class BusinessNews {
    private HashMap<Integer,News> businessNews = new HashMap<>();

    public void addBusinessNews(Integer id,String title,String pub,String URL)
    {
        News newNews = new News(title,pub,URL);
        businessNews.put(id,newNews);
    }
}
