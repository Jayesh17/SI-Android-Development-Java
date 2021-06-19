package com.example.watchnews;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    public static Context mainContext;
    public static MainController mainController;
    String[] catArr = new String[7];
    List<HashMap<String,String>> HashMapNewsList;
    String[] colNames = {"Title","PublishTime"};
    int[] showAt = {R.id.newsTitle,R.id.pubTime};

    public Handler customHandler;
    ListView newsListView;
    ArrayList<HashMap<String,String>> newsList;
    TextView status;
    Spinner topics;
    SimpleAdapter adapter;

    public void setInitialState()
    {
        catArr = getResources().getStringArray(R.array.categories);
        mainContext = getApplicationContext();
        mainController = new MainController();
        status = findViewById(R.id.Status);
        status.setVisibility(View.VISIBLE);
        topics = findViewById(R.id.cats);
        newsListView = findViewById(R.id.News);
        customHandler = new Handler();
        newsList = new ArrayList<>();
        BGTask bgTask = new BGTask();
        bgTask.execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialState();
        checkStatusAndProceed();
    }

    //top-busi-health-entern-sci-sports-tech
    //this will associate approprite data to adapter and list
    public void getDataAndShow(String topic)
    {
        if(topic.equals(catArr[0]))
        {
                HashMapNewsList = MainController.getListForTopNews();
        }
        else if(topic.equals(catArr[1]))
        {
            HashMapNewsList = MainController.getListForBusinessNews();
        }
        else if(topic.equals(catArr[2]))
        {
            HashMapNewsList = MainController.getListForHealthNews();
        }
        else if(topic.equals(catArr[3]))
        {
            HashMapNewsList = MainController.getListForEnternNews();
        }
        else if(topic.equals(catArr[4]))
        {
            HashMapNewsList = MainController.getListForScienceNews();
        }
        else if(topic.equals(catArr[5]))
        {
            HashMapNewsList = MainController.getListForSportsNews();
        }
        else if(topic.equals(catArr[6]))
        {
            HashMapNewsList = MainController.getListForTechNews();
        }
        else {
            Log.d("find","Not Found");
        }

        adapter = new SimpleAdapter(this,HashMapNewsList,R.layout.activity_list_item,colNames,showAt);
        newsListView.setAdapter(adapter);
    }

    //This will get all the saved data and set to view
    public void getLists()
    {
        List<HashMap<String,String>> defaultHashMapNewsList = MainController.getListForTopNews();
        SimpleAdapter adapter = new SimpleAdapter(this,defaultHashMapNewsList,R.layout.activity_list_item,colNames,showAt);
        newsListView.setAdapter(adapter);

        topics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String topic = topics.getSelectedItem().toString();
                getDataAndShow(topic);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = ((TextView)view.findViewById(R.id.newsTitle)).getText().toString();
                String itemURL = MainController.titlesWithURLs.get(title);
                Uri uri = Uri.parse(itemURL);
                Intent visitArticle = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(visitArticle);
                Toast.makeText(getApplicationContext(),"Redirect to the full Article",Toast.LENGTH_LONG).show();
            }
        });

    }


    //This will check the status of API data and initiate further task
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkStatusAndProceed()
    {
        Runnable checkIfLoaded = new Runnable() {
            @Override
            public void run() {
                if(!MainController.isdone)
                {
                    Log.d("delay","in delay");
                    customHandler.postDelayed(this,100);
                }
                else
                {
                    status.setVisibility(View.GONE);
                    customHandler.removeCallbacksAndMessages(null);
                    getLists();
                }
            }
        };
        customHandler.postDelayed(checkIfLoaded,100);

    }

}