package com.example.watchnews.NewsPKG;

import android.net.wifi.WpsInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.watchnews.MainActivity;
import com.example.watchnews.MainController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HealthNews {

    private HashMap<Integer,News> healthNews;
    private int count;

    public HealthNews() {
        healthNews = new HashMap<>();
        count=0;

    }
    public void addHealthNews(Vector<News> newsList)
    {
        int id=0;
        Iterator<News> it = newsList.iterator();
        while (it.hasNext())
        {
            News n = it.next();
            id++;
            count++;
            healthNews.put(id,n);
        }
    }

    public HashMap<Integer, News> getHealthNews() {
        return healthNews;
    }

    public void showTitle()
    {
        Iterator<Map.Entry<Integer, News>> it = healthNews.entrySet().iterator();
       while(it.hasNext())
        {
            Map.Entry<Integer,News> en = it.next();
            News n = en.getValue();
            Log.d("Success",+en.getKey()+" "+n.getTitle());
        }
    }
}
