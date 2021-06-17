package com.example.watchnews;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.watchnews.NewsPKG.HealthNews;

public class MainController {

    public static RequestQueue APIRequests = Volley.newRequestQueue(MainActivity.mainContext);

    public void doIt()
    {
        HealthNews h = new HealthNews();
        h.getDataFromAPI("https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=6d02c290b6a94d2b9b8e2cc1571070f2");

        h.setData();
        h.showTitle();
    }
}
