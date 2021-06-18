package com.example.watchnews.NewsPKG;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class BusinessNews {
    private HashMap<Integer,News> businessNews;
    private int count;

    public BusinessNews()
    {
        businessNews = new HashMap<>();
        count=0;
    }
    public void addBusinessNews(Vector<News> newsList)
    {
        int id=0;
        Iterator<News> it = newsList.iterator();
        while (it.hasNext())
        {
            News n = it.next();
            id++;
            count++;
            businessNews.put(id,n);
        }
    }
}
