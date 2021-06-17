package com.example.watchnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static Context mainContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = getApplicationContext();

        MainController m = new MainController();
        m.doIt();
    }


}