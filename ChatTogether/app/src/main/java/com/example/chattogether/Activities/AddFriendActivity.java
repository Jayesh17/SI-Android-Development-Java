package com.example.chattogether.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chattogether.Adapters.ContactAdapter;
import com.example.chattogether.Models.Contact;
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
import java.util.HashSet;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    public static Context context;

    RecyclerView contactsListView;
    TextView per_status;
    ProgressDialog per_dialog;

    List<Contact> contactList;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference friendsReference;

    HashMap<String,String> contactMap;
    ContactAdapter adapter;
    HashSet<String> friends;

    final int CONTACT_PERMISSION_CODE = 1;

    public static Activity act;
    public static Context app_context;

    public void setInitialState()
    {
        context = AddFriendActivity.this;
        app_context = getApplicationContext();
        contactsListView = findViewById(R.id.contactsListView);
        contactsListView.setLayoutManager(new LinearLayoutManager(this));
        per_status = findViewById(R.id.per_status);
        auth = FirebaseAuth.getInstance();
        act = AddFriendActivity.this;

        contactList = new ArrayList<>();
        contactMap = new HashMap<>();
        friends = new HashSet<>();

        adapter = new ContactAdapter(AddFriendActivity.this,contactList);


        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference().child("user");
        friendsReference = database.getReference().child("FriendList").child(auth.getUid());

        per_dialog = new ProgressDialog(this);
        per_dialog.setCancelable(false);
        per_dialog.setMessage("Fetching Contacts...");
        per_dialog.show();

        per_status.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setInitialState();

        friendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    String userId = ds.getValue(String.class);
                    Log.d("FRIENDS",userId);
                    friends.add(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // to get all the child of database.
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    User user = ds.getValue(User.class);
                    if(!friends.contains(user.getUID()))
                    {
                        //Log.d("FRIENDS",user.getUID());
                        contactMap.put(user.getPhone(),user.getUID());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkContactPermission();
            }
        },500);
    }

    public void getContactPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddFriendActivity.this, Manifest.permission.READ_CONTACTS)) {
            new AlertDialog.Builder(this).setTitle("Contact Permission needed.")
                    .setMessage("Contact permission is needed to fetch all the contacts registered on Chat Together App.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddFriendActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PERMISSION_CODE);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //hasPermission = false;
                    per_dialog.dismiss();
                    per_status.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(AddFriendActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PERMISSION_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContactDetails();
            }
            else {
                Toast.makeText(getBaseContext(),"Permission denied.",Toast.LENGTH_SHORT).show();
                per_dialog.dismiss();
                per_status.setVisibility(View.VISIBLE);
            }
        }
    }


    private void checkContactPermission() {

        //check if permission is already granted to us or not.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            getContactDetails();
        } else {
            getContactPermission();
        }
    }


    private void getContactDetails() {

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext())
        {
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            number = number.replaceAll("\\s", "");
            number = number.replaceAll("-", "");
            String num = number.substring(3);
            //Log.d("SAVED_CONTACTS",num+" "+number);
            if(contactMap.containsKey(number) || contactMap.containsKey(num))
            {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String uid = contactMap.get(number);

                Contact contact = new Contact(name,number,uid);
                    contactList.add(contact);
                    contactMap.remove(number);
            }
            adapter.notifyDataSetChanged();
        }
        per_dialog.dismiss();
        if(contactList.isEmpty())
        {
            per_status.setText("Not Found any Registered Users Or You have added all of them.");
            per_status.setVisibility(View.VISIBLE);
        }
        contactsListView.setAdapter(adapter);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddFriendActivity.this,HomeActivity.class));
        finish();
    }
}