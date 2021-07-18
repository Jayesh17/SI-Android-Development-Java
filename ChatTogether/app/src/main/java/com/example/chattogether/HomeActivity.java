package com.example.chattogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chattogether.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView chatList;
    UserAdapter adapter;
    ImageView logOutBtn;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private List<User> users;

    public static String CHAT_USER_NAME = "com.example.chattogether.HomeActivity.USER_NAME";
    public static String CHAT_USER_PROFILE = "com.example.chattogether.HomeActivity.USER_PROFILE";
    public static String CHAT_USER_ID = "com.example.chattogether.HomeActivity.USER_ID";

    public HomeActivity() {
    }

    public void setInitialState()
    {
        logOutBtn = findViewById(R.id.logOutBtn);
        chatList = findViewById(R.id.chatList);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        users = new ArrayList<>();

        adapter = new UserAdapter(HomeActivity.this,users);
        chatList.setAdapter(adapter);

        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference().child("user");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //means current user has not logged in or he has been logged out
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            finish();
        }

        setInitialState();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // to get all the child of database.
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    User user = ds.getValue(User.class);
                    users.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(HomeActivity.this,R.style.logOutDialog);
                dialog.setContentView(R.layout.layout_logout_dialog);

                TextView yesBtn, noBtn;
                yesBtn = dialog.findViewById(R.id.logoutYesBtn);
                noBtn = dialog.findViewById(R.id.logoutNoBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth.signOut();
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }
}