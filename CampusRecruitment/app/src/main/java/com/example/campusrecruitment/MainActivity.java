package com.example.campusrecruitment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.example.campusrecruitment.BackGroundTasks.InitBGTask;
import com.example.campusrecruitment.BackGroundTasks.MailBGTasks;
import com.example.campusrecruitment.BackGroundTasks.MailManagement;
import com.example.campusrecruitment.DBManipulation.DBHandler;

public class MainActivity extends AppCompatActivity {

    public static MailBGTasks mailBGTasks;
    public static DBHandler dbHandler;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialState();
    }

    public void setInitialState()
    {
        mailBGTasks = new MailBGTasks();
        context = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
        InitBGTask createDB = new InitBGTask();
        createDB.execute();
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