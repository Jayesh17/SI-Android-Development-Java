package com.example.chattogether.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chattogether.Models.User;
import com.example.chattogether.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView currUserProfile;
    EditText currUserName;
    EditText currUserStatus;
    EditText currUserMail;
    EditText currUserPhone;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    TextView saveBtn;

    int STORAGE_PERMISSION_CODE = 1;

    String email;
    String phone;
    String name;
    String status;
    String profileUri;

    String newName;
    String newStatus;
    String newProfileStr;
    Uri newProfile;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setInitialState();

        DatabaseReference databaseReference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name = snapshot.child("name").getValue().toString();
                phone = snapshot.child("phone").getValue().toString();
                email = snapshot.child("email").getValue().toString();
                profileUri = snapshot.child("profileUri").getValue().toString();
                status = snapshot.child("status").getValue().toString();

                currUserName.setText(""+name);
                currUserStatus.setText(""+status);
                currUserMail.setText(""+email);
                currUserPhone.setText(""+phone);
                Picasso.get().load(profileUri).into(currUserProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                newName=currUserName.getText().toString();
                newStatus = currUserStatus.getText().toString();
                Log.d("newName",newName+newStatus);
                if(newProfile!=null)
                {
                    storageReference.putFile(newProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newProfileStr = uri.toString();
                                    User user = new User(newName,email,phone,newStatus,newProfileStr,auth.getUid());
                                    databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    dialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this,"Profile Updated Successfully.",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                                                    finish();
                                                }
                                                else {
                                                    dialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this,"Something Went Wrong.",Toast.LENGTH_LONG).show();
                                                }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newProfileStr = uri.toString();
                            User user = new User(newName,email,phone,newStatus,newProfileStr,auth.getUid());
                            databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(SettingsActivity.this,"Profile Updated Successfully.",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                                        finish();
                                    }
                                    else {
                                        dialog.dismiss();
                                        Toast.makeText(SettingsActivity.this,"Something Went Wrong.",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        currUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });
    }

    public void checkStoragePermission() {
        //check if permission is alreay granted to us or not.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(),"Opening Media",Toast.LENGTH_SHORT).show();
            pickProfile();
        } else {
            getStoragePermission();
           }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getBaseContext(),"Opening Media",Toast.LENGTH_SHORT).show();
                pickProfile();
            }
            else {
                Toast.makeText(getBaseContext(),"Permission denied.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this).setTitle("Storage permission needed")
                    .setMessage("Storage permission is needed to read Image Files from your device.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //hasPermission = false;
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    public void pickProfile()
    {
        mGetContent.launch("image/*");
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    currUserProfile.setImageURI(uri);
                    newProfile = uri;
                }
            });


    private void setInitialState() {

        currUserProfile = findViewById(R.id.currUserProfile);
        currUserName = findViewById(R.id.currUserName);
        currUserStatus = findViewById(R.id.currUserStatus);
        currUserMail = findViewById(R.id.currUserMail);
        currUserPhone = findViewById(R.id.currUserPhone);

        saveBtn = findViewById(R.id.saveBtn);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait..");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        finish();
    }
}