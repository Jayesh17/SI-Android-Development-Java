package com.example.adminpanel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adminpanel.AdminBGTasks.InitializeDB;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setInitialState();
    }

    private void setInitialState() {
        InitializeDB initializeDB = new InitializeDB();
        initializeDB.execute();
    }
}