package com.example.watchnews.NewsPKG;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    public Vector<String> getURLs()
    {
        Vector<String> URLs = new Vector<>();
        Iterator<Map.Entry<Integer, News>> it = topNews.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<Integer,News> en = it.next();
            News n = en.getValue();
            URLs.add(n.getURL());
        }
        return URLs;
    }

    public List<HashMap<String,String>> getRequiredInfo()
    {
        List<HashMap<String,String>> NewsListData = new ArrayList<>();
        Iterator<Map.Entry<Integer, News>> it = topNews.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<Integer,News> en = it.next();
            News n = en.getValue();
            HashMap<String, String> newEntry = new HashMap<>();
            newEntry.put("Title",n.getTitle());
            newEntry.put("PublishTime",n.getPublishTime());
            NewsListData.add(newEntry);
        }
        return NewsListData;
    }
}
