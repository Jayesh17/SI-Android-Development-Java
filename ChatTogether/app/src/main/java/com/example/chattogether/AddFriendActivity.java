package com.example.chattogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.chattogether.Models.Contact;
import com.example.chattogether.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    public static Context context;

    RecyclerView contactsListView;

    List<Contact> contactList;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    HashMap<String,String> contactMap;

    public void setInitialState()
    {
        context = AddFriendActivity.this;
        contactsListView = findViewById(R.id.contactsListView);
        contactList = new ArrayList<>();
        contactMap = new HashMap<>();

        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference().child("user");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setInitialState();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // to get all the child of database.
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    User user = ds.getValue(User.class);
                    contactMap.put(user.getPhone(),user.getUID());   
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddFriendActivity.this,HomeActivity.class));
        finish();
    }
}