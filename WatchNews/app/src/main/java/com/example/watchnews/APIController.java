package com.example.watchnews;

import android.util.Log;

import com.example.watchnews.NewsPKG.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class APIController {

    HttpURLConnection urlConnection;
    public String getDataFromAPI(String linkURL)
    {
        String result="";
        try
        {
            URL url = new URL(linkURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            int code = urlConnection.getResponseCode();
            Log.d("code",code+"");

            if(code == 200)
            {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                if(in != null)
                {
                    InputStreamReader inReader = new InputStreamReader(in);
                    BufferedReader reader = new BufferedReader(inReader);
                    String line="";

                    while ((line = reader.readLine())!=null)
                    {
                        result+=line;
                    }
                }
                in.close();
            }
            else
            {
                Log.d("er","error code "+code);
            }
        } catch (MalformedURLException e) {
            Log.d("apiurler",e.toString());
        } catch (IOException e) {
            Log.d("apiurler",e.toString());
        }
        return result;
    }
    public Vector<News> prepareData(String data)
    {
        Vector<News> newsList = new Vector<>();
        try {
            JSONObject dataObj = new JSONObject(data);
            JSONArray articles = dataObj.getJSONArray("articles");
            for (int i = 0; i < articles.length() ; i++) {

                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                String URL = article.getString("url");
                String pub = article.getString("publishedAt");

                News news = new News(title,URL,pub);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.d("jsonerr",e.toString());
        }
        return newsList;
    }
}
