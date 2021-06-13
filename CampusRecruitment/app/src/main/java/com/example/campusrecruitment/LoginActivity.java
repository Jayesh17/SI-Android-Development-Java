package com.example.campusrecruitment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setIntiateState();
    }
    public void setIntiateState()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
    }
    public void regAct(View view)
    {
        Intent reg = new Intent(this, RegisterActivity.class);
        startActivity(reg);
    }
    public void forgotPassAct(View view)
    {
        Intent fp = new Intent(this, ForgotPasswordActivity.class);
        startActivity(fp);
    }
}