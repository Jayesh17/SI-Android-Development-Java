package com.example.campusrecruitment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setIntiateState();
    }

    public void setIntiateState()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
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