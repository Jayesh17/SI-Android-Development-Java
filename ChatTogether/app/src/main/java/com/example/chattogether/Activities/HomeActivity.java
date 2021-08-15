package com.example.chattogether.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chattogether.Adapters.UserAdapter;
import com.example.chattogether.Models.User;
import com.example.chattogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView chatList;
    UserAdapter adapter;
    ImageView logOutBtn;
    TextView fr_status;

    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceFriendList;
    private DatabaseReference databaseReferenceUser;

    private List<User> users;

    ProgressDialog progressDialog;

    public static String CHAT_USER_NAME = "com.example.chattogether.Activities.HomeActivity.USER_NAME";
    public static String CHAT_USER_PROFILE = "com.example.chattogether.Activities.HomeActivity.USER_PROFILE";
    public static String CHAT_USER_ID = "com.example.chattogether.Activities.HomeActivity.USER_ID";

    public static String lastMsg;

    boolean doublePressToExit;
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
        lastMsg = "";
        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
        Log.d("HOME_C","aaaaa"+auth.getCurrentUser());
        databaseReferenceFriendList = database.getReference().child("FriendList").child(auth.getUid());
        databaseReferenceUser = database.getReference().child("user");
        doublePressToExit = false;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait..");
        fr_status = findViewById(R.id.fr_status);
        fr_status.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //means current user has not logged in or he has been logged out
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()==null)
        {
            Log.d("HOME_C","aaaaa"+auth.getCurrentUser());
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            finish();
        }
        else {
            setInitialState();

            progressDialog.show();
            databaseReferenceFriendList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists())
                    {
                        progressDialog.dismiss();
                    }
                    else {
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            String userID  = ds.getValue(String.class);
                            databaseReferenceUser.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HashMap<String,String> values = new HashMap<>();
                                    for(DataSnapshot ds : snapshot.getChildren())
                                    {
                                        values.put(ds.getKey(),ds.getValue(String.class));
                                    }

                                    User user = new User(values.get("name"),values.get("email"),values.get("phone"),values.get("status"),values.get("profileUri"),values.get("uid"));
                                    Log.d("ID",user.getUID());
                                    users.add(user);
                                    fr_status.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                }
                            });
                            progressDialog.dismiss();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
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
                            Log.d("USERCURRENT",auth.getCurrentUser()+"");
                            dialog.dismiss();
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

    @Override
    public void onBackPressed() {

        if(doublePressToExit)
        {
            super.onBackPressed();
            finish();
        }else {
            Toast.makeText(HomeActivity.this,"Press Back again to Exit.",Toast.LENGTH_LONG).show();
            doublePressToExit = true;
        }
    }

    public void redirectSettings(View view)
    {
        overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
        startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
        finish();
    }
    public void redirectAddFriends(View view)
    {
        overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
        startActivity(new Intent(HomeActivity.this,AddFriendActivity.class));
        finish();
    }
}