package com.example.watchnews.NewsPKG;
import java.util.HashMap;

public class TechNews {
    private HashMap<Integer,News> techNews = new HashMap<>();
    public void addTechNews(Integer id,String title,String pub,String URL)
    {
        News newNews = new News(title,pub,URL);
        techNews.put(id,newNews);
    }

}
