package com.example.watchnews.NewsPKG;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;

import java.util.HashMap;

public class TopNews {
    private HashMap<Integer,News> topNews = new HashMap<>();

    public void addTopHeadlines(Integer id,String title,String pub,String URL)
    {
        News newNews = new News(title,pub,URL);
        topNews.put(id,newNews);
    }

    public HashMap<Integer, News> getTopNews() {
        return topNews;
    }

}
