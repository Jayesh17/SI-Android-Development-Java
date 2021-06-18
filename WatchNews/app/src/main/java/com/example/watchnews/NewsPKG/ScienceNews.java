package com.example.watchnews.NewsPKG;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class ScienceNews {
    private HashMap<Integer,News> scienceNews;
    private int count;

    public ScienceNews()
    {
        scienceNews = new HashMap<>();
        count=0;
    }
    public void addScienceNews(Vector<News> newsList)
    {
        int id=0;
        Iterator<News> it = newsList.iterator();
        while (it.hasNext())
        {
            News n = it.next();
            id++;
            count++;
            scienceNews.put(id,n);
        }
    }
}
