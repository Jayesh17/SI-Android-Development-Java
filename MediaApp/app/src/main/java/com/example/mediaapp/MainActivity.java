package com.example.mediaapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    public static boolean isLoaded;
    public static MediaController mediaController;
    TextView status;
    Handler customHandler;
    ListView songList;


    public void setIntialState()
    {
        context = getApplicationContext();
        isLoaded = false;
        songList = findViewById(R.id.list);
        mediaController = new MediaController();
        BackGroundTask BGTask = new BackGroundTask();
        BGTask.execute();
        status = findViewById(R.id.status);
        status.setVisibility(View.VISIBLE);
        customHandler = new Handler();
        customHandler.postDelayed(checkIfDone,0);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setIntialState();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getListView()
    {
        List<String> songs = mediaController.getNames();
        ArrayAdapter<String> songsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,songs);
        songList.setAdapter(songsAdapter);
    }
    private Runnable checkIfDone = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            if(isLoaded == false)
            {
                customHandler.postDelayed(this,10);
            }
            else
            {
                status.setVisibility(View.GONE);
                getListView();
            }
        }
    };
}