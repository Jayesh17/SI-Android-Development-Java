package com.example.campusrecruitment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static MainController controller;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialState();
    }

    public void setInitialState()
    {
        context = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
        controller = new MainController();
    }
    public void loginAct(View view)
    {
        Intent Login = new Intent(this,LoginActivity.class);
        startActivity(Login);
    }
    public void registerAct(View view)
    {
        Intent reg = new Intent(this,RegisterActivity.class);
        startActivity(reg);
    }
}