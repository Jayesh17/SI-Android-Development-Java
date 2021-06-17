package com.example.watchnews.NewsPKG;

import java.util.HashMap;

public class SportsNews {

    private HashMap<Integer,News> sportsNews = new HashMap<>();

    public void addSportsNews(Integer id,String title,String pub,String URL)
    {
        News newNews = new News(title,pub,URL);
        sportsNews.put(id,newNews);
    }

}
