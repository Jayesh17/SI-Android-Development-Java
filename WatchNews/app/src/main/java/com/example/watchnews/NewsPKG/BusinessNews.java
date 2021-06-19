package com.example.watchnews.NewsPKG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    public List<HashMap<String, String>> getRequiredInfo() {
        List<HashMap<String,String>> NewsListData = new ArrayList<>();
        Iterator<Map.Entry<Integer, News>> it = businessNews.entrySet().iterator();
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
