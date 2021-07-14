package com.example.chattogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class CheckAuthActivity extends AppCompatActivity {

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_auth);

        //means current user has not logged in or he has been logged out
        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(CheckAuthActivity.this,RegistrationActivity.class));
        }
    }
}