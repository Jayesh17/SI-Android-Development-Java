package com.example.watchnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static Context mainContext;
    public static MainController mainController;

    public void setInitialState()
    {
        mainContext = getApplicationContext();
        mainController = new MainController();
        BGTask bgTask = new BGTask();
        bgTask.execute();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialState();
    }


}