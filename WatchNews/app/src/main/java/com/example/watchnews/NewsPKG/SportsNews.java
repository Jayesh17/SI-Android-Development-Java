package com.example.watchnews.NewsPKG;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class SportsNews {

    private HashMap<Integer,News> sportsNews;
    private int count;

    public SportsNews()
    {
        sportsNews = new HashMap<>();
        count=0;
    }
    public void addSportsNews(Vector<News> newsList)
    {
        int id=0;
        Iterator<News> it = newsList.iterator();
        while (it.hasNext())
        {
            News n = it.next();
            id++;
            count++;
            sportsNews.put(id,n);
        }
    }
}
