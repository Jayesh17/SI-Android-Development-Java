package com.example.campusrecruitment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setIntiateState();
    }
    public void studentRegAct(View view)
    {
        Intent regStud = new Intent(this, StudentRegisterActivity.class);
        startActivity(regStud);
    }
    public void setIntiateState()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
    }
}