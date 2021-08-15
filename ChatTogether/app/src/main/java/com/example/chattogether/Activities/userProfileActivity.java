package com.example.chattogether.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chattogether.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class userProfileActivity extends AppCompatActivity {

    TextView userProfileName;
    TextView userProfilePhone;
    TextView userProfileStatus;
    TextView userProfileEmail;
    ImageView userProfilePic;

    String userID;

    FirebaseDatabase database;

    public void setInitialState()
    {
        Intent intent = getIntent();
        userID = intent.getStringExtra(ChatActivity.USER_ID);

        userProfileName = findViewById(R.id.userProfileName);
        userProfilePhone = findViewById(R.id.userProfilePhone);
        userProfileStatus = findViewById(R.id.userProfileStatus);
        userProfileEmail = findViewById(R.id.userProfileEmail);
        userProfilePic = findViewById(R.id.userProfilePic);

        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setInitialState();

        DatabaseReference userRef = database.getReference().child("user").child(userID);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String,String> values = new HashMap<>();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    values.put(ds.getKey(),ds.getValue(String.class));
                }

                userProfileName.setText(values.get("name"));
                userProfileEmail.setText(values.get("email"));
                userProfilePhone.setText(values.get("phone"));
                userProfileStatus.setText(values.get("status"));
                //userProfilePic.setImageURI(Uri.parse(values.get("profileUri")));

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference().child("upload");

                String profile = values.get("profileUri");
                if(profile.equals("https://firebasestorage.googleapis.com/v0/b/chattogether-19397.appspot.com/o/profile.png?alt=media&token=55170d34-4f1f-4161-9bcb-6b0d4f9e7ad5")) {
                    Picasso.get().load(profile).into(userProfilePic);
                }
                else {
                    storageReference.child(userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            Log.d("URIP",uri.toString());
                            Picasso.get().load(uri.toString()).into(userProfilePic);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}