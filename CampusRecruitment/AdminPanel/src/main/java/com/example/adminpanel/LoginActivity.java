package com.example.adminpanel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adminpanel.AdminBGTasks.GetAccessOfDB;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setInitialState();
    }

    private void setInitialState() {

        GetAccessOfDB access = new GetAccessOfDB(getApplicationContext());
        access.execute();
    }
}