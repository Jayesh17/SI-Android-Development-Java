package com.example.watchnews.NewsPKG;
import java.util.HashMap;

public class ScienceNews {
    private HashMap<Integer,News> scienceNews = new HashMap<>();
    public void addScienceNews(Integer id,String title,String pub,String URL)
    {
        News newNews = new News(title,pub,URL);
        scienceNews.put(id,newNews);
    }

}
