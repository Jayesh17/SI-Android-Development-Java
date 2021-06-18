package com.example.watchnews.NewsPKG;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class EnternNews {
    private HashMap<Integer,News> enternNews;
    private int count;
    public EnternNews()
    {
        enternNews = new HashMap<>();
        count=0;
    }
    public void addEnternNews(Vector<News> newsList)
    {
        int id=0;
        Iterator<News> it = newsList.iterator();
        while (it.hasNext())
        {
            News n = it.next();
            id++;
            count++;
            enternNews.put(id,n);
        }
    }


    public HashMap<Integer, News> getEnternNews() {
        return enternNews;
    }

}
