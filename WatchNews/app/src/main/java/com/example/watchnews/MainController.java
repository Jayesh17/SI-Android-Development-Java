package com.example.watchnews;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.watchnews.NewsPKG.BusinessNews;
import com.example.watchnews.NewsPKG.EnternNews;
import com.example.watchnews.NewsPKG.HealthNews;
import com.example.watchnews.NewsPKG.News;
import com.example.watchnews.NewsPKG.ScienceNews;
import com.example.watchnews.NewsPKG.SportsNews;
import com.example.watchnews.NewsPKG.TechNews;
import com.example.watchnews.NewsPKG.TopNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

public class MainController {

    public static HealthNews healthNews;
    public static TopNews topNews;
    public static EnternNews enternNews;
    public static BusinessNews businessNews;
    public static ScienceNews scienceNews;
    public static TechNews techNews;
    public static SportsNews sportsNews;
    public static APIController apiController;

    public static boolean isdone;
    public static HashMap<String,String> titlesWithURLs;

    public MainController()
    {
        isdone = false;
        healthNews = new HealthNews();
        topNews = new TopNews();
        enternNews = new EnternNews();
        businessNews = new BusinessNews();
        scienceNews = new ScienceNews();
        techNews = new TechNews();
        sportsNews = new SportsNews();
        apiController = new APIController();
        titlesWithURLs = new HashMap<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void populateData()
    {
        String URLs[] =
                {
                        "https://newsapi.org/v2/top-headlines?country=in&apiKey=6d02c290b6a94d2b9b8e2cc1571070f2",
                        "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=6d02c290b6a94d2b9b8e2cc1571070f2",
                        "https://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=6d02c290b6a94d2b9b8e2cc1571070f2",
                        "https://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=6d02c290b6a94d2b9b8e2cc1571070f2",
                        "https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=6d02c290b6a94d2b9b8e2cc1571070f2",
                        "https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=6d02c290b6a94d2b9b8e2cc1571070f2",
                        "https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=6d02c290b6a94d2b9b8e2cc1571070f2"
                };

       CompletableFuture<Vector<News>> futures[] = new CompletableFuture[7];
        for (int i = 0; i < URLs.length; i++) {
            int finalI = i;
             futures[i] = CompletableFuture.supplyAsync(()->apiController.getDataFromAPI(URLs[finalI])).thenApply(newsData -> apiController.prepareData(newsData));
        }

        Vector<News> newsLists[] = new Vector[7];
        for (int i = 0; i < URLs.length; i++) {
            try
            {
                newsLists[i] = futures[i].get();
            }
            catch (Exception e)
            {
                Log.d("geturlerr",e.toString());
            }
        }

        CompletableFuture<Void> prepareFuture[] = new CompletableFuture[7];

        prepareFuture[0]= CompletableFuture.runAsync(()->{
           topNews.addTopNews(newsLists[0]);
        });

        prepareFuture[1]= CompletableFuture.runAsync(()->{
            businessNews.addBusinessNews(newsLists[1]);
        });

        prepareFuture[2]= CompletableFuture.runAsync(()->{
            enternNews.addEnternNews(newsLists[2]);
        });

        prepareFuture[3]= CompletableFuture.runAsync(()->{
            healthNews.addHealthNews(newsLists[3]);
        });

        prepareFuture[4]= CompletableFuture.runAsync(()->{
            scienceNews.addScienceNews(newsLists[4]);
        });

        prepareFuture[5]= CompletableFuture.runAsync(()->{
            sportsNews.addSportsNews(newsLists[5]);
        });

        prepareFuture[6]= CompletableFuture.runAsync(()->{
            techNews.addTechNews(newsLists[6]);
        });


        for (int i = 0; i < prepareFuture.length; i++) {

            try
            {
                prepareFuture[i].get();
                isdone = true;
            }
            catch (Exception e)
            {

                Log.d("prepareerr",e.toString());
            }
        }
        healthNews.showTitle();
    }

    public static List<HashMap<String,String>> getListForTopNews()
    {
        return topNews.getRequiredInfo();
    }

    public static List<HashMap<String, String>> getListForBusinessNews() {
        return businessNews.getRequiredInfo();
    }

    public static List<HashMap<String, String>> getListForHealthNews() {
        return healthNews.getRequiredInfo();
    }

    public static List<HashMap<String, String>> getListForEnternNews() {
        return enternNews.getRequiredInfo();
    }

    public static List<HashMap<String, String>> getListForScienceNews() {
        return scienceNews.getRequiredInfo();
    }

    public static List<HashMap<String, String>> getListForSportsNews() {
        return sportsNews.getRequiredInfo();
    }

    public static List<HashMap<String, String>> getListForTechNews() {
        return techNews.getRequiredInfo();
    }

}
