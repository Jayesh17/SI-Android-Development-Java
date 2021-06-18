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

    public MainController()
    {
        healthNews = new HealthNews();
        topNews = new TopNews();
        enternNews = new EnternNews();
        businessNews = new BusinessNews();
        scienceNews = new ScienceNews();
        techNews = new TechNews();
        sportsNews = new SportsNews();
        apiController = new APIController();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void populateData()
    {
        CompletableFuture<String> newsFuture[] = new CompletableFuture[7];
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

        for (int i = 0; i < URLs.length; i++) {
            int finalI = i;
            newsFuture[i] = CompletableFuture.supplyAsync(()->{
                return apiController.getDataFromAPI(URLs[finalI]);
            });
        }

        String resData[] = new String[7];

        for (int i = 0; i < URLs.length; i++) {
            try
            {
                resData[i] = new String();
                resData[i] = newsFuture[i].get();
                //newsLists[i] = apiController.prepareData(newsFuture[i].get());
            }
            catch (Exception e)
            {
                Log.d("geturler",e.toString());
            }
        }

        //top,business,entern,health,sci,sport,tech

        Vector<News> topNewsList = apiController.prepareData(resData[0]);
        Vector<News> businessNewsList = apiController.prepareData(resData[1]);
        Vector<News> enternNewsList = apiController.prepareData(resData[2]);
        Vector<News> healthNewsList = apiController.prepareData(resData[3]);
        Vector<News> sciNewsList = apiController.prepareData(resData[4]);
        Vector<News> sportNewsList = apiController.prepareData(resData[5]);
        Vector<News> techNewsList = apiController.prepareData(resData[6]);


        CompletableFuture<Void> prepareFuture[] = new CompletableFuture[7];

        prepareFuture[0]= CompletableFuture.runAsync(()->{
           topNews.addTopNews(topNewsList);
        });

        prepareFuture[1]= CompletableFuture.runAsync(()->{
            businessNews.addBusinessNews(businessNewsList);
        });

        prepareFuture[2]= CompletableFuture.runAsync(()->{
            enternNews.addEnternNews(enternNewsList);
        });

        prepareFuture[3]= CompletableFuture.runAsync(()->{
            healthNews.addHealthNews(healthNewsList);
        });

        prepareFuture[4]= CompletableFuture.runAsync(()->{
            scienceNews.addScienceNews(sciNewsList);
        });

        prepareFuture[5]= CompletableFuture.runAsync(()->{
            sportsNews.addSportsNews(sportNewsList);
        });

        prepareFuture[6]= CompletableFuture.runAsync(()->{
            techNews.addTechNews(techNewsList);
        });


        for (int i = 0; i < prepareFuture.length; i++) {

            try
            {
                prepareFuture[i].get();
            }
            catch (Exception e)
            {
                Log.d("purler",e.toString());
            }
        }

        healthNews.showTitle();
    }
}
