package com.example.chattogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ChatActivity extends AppCompatActivity {

    String receiverProfile;
    String receiverName;
    String receiverUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setInitialState();
    }

    private void setInitialState() {
        Intent intent = getIntent();
        receiverName = intent.getStringExtra(HomeActivity.CHAT_USER_NAME);
        receiverUID = intent.getStringExtra(HomeActivity.CHAT_USER_ID);
        receiverProfile = intent.getStringExtra(HomeActivity.CHAT_USER_PROFILE);
    }
}