package com.example.watchnews.NewsPKG;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class TopNews {
    private HashMap<Integer,News> topNews;
    private int count;

    public TopNews()
    {
        topNews = new HashMap<>();
        count=0;
    }
    public void addTopNews(Vector<News> newsList)
    {
        int id=0;
        Iterator<News> it = newsList.iterator();
        while (it.hasNext())
        {
            News n = it.next();
            id++;
            count++;
            topNews.put(id,n);
        }
    }

    public HashMap<Integer, News> getTopNews() {
        return topNews;
    }

}
