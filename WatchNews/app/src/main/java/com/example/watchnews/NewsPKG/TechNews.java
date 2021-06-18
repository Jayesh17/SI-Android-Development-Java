package com.example.watchnews.NewsPKG;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class TechNews {
    private HashMap<Integer,News> techNews;
    private int count;

    public TechNews()
    {
        techNews = new HashMap<>();
        count=0;
    }
    public void addTechNews(Vector<News> newsList)
    {
        int id=0;
        Iterator<News> it = newsList.iterator();
        while (it.hasNext())
        {
            News n = it.next();
            id++;
            count++;
            techNews.put(id,n);
        }
    }
}
