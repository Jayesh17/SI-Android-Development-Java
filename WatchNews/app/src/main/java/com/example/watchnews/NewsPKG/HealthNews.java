package com.example.watchnews.NewsPKG;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HealthNews {

    private HashMap<Integer,News> healthNews;
    RequestFuture<JSONObject> future;

    public HealthNews() {
        future = RequestFuture.newFuture();
        healthNews = new HashMap<>();

    }

    public void addHealthNews(Integer id, String title, String pub, String URL)
    {
        News newNews = new News(title,pub,URL);
        healthNews.put(id,newNews);
    }

    public HashMap<Integer, News> getHealthNews() {
        return healthNews;
    }

    public void getDataFromAPI(String URL)
    {
        JsonObjectRequest newRequest = new JsonObjectRequest(URL,new JSONObject(),future,future);
        MainController.APIRequests.add(newRequest);
    }
    public void setData()
    {
        int count=0;
        try
        {
            JSONObject obj = future.get();
            JSONArray articles = obj.getJSONArray("articles");
            for (int i = 0; i < articles.length() ; i++) {

                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                String URL = article.getString("url");
                String pub = article.getString("publishedAt");

                count++;
                addHealthNews(count,title,pub,URL);
            }
        }
        catch (InterruptedException | ExecutionException err)
        {
            Log.d("Error","something Went Wrong "+err);
        }
        catch (Exception e)
        {
            Log.d("Error","something Went Wrong "+e);
        }
    }

    public void showTitle()
    {
        Iterator<Map.Entry<Integer, News>> it = healthNews.entrySet().iterator();
       /* while(it.hasNext())
        {*/
            Map.Entry<Integer,News> en = it.next();
            News n = en.getValue();
            Log.d("Success",+en.getKey()+" "+n.getTitle());
        //}
    }
}
