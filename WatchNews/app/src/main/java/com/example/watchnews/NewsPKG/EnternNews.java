package com.example.watchnews.NewsPKG;
import java.util.HashMap;

public class EnternNews {
    private HashMap<Integer,News> enternNews = new HashMap<>();

    public void addEnternNews(Integer id,String title,String pub,String URL)
    {
        News newNews = new News(title,pub,URL);
        enternNews.put(id,newNews);
    }

    public HashMap<Integer, News> getEnternNews() {
        return enternNews;
    }

}
