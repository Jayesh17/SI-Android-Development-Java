package com.example.chattogether.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chattogether.Models.User;
import com.example.chattogether.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserAuthActivity extends AppCompatActivity {

    TextInputLayout reg_otp;
    TextInputEditText otpView;
    Button userAuthBtn;
    ProgressDialog dialog;

    String OTP;
    String Email;
    String name;
    String pass;
    String phone;
    String status;
    String profileUri;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    public void setInitialState()
    {
        reg_otp = findViewById(R.id.reg_otp);
        otpView = findViewById(R.id.otpView);
        userAuthBtn = findViewById(R.id.userAuthBtn);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait..");
        dialog.setCancelable(false);

        Intent getIntent = getIntent();
        Email = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_KEY);
        OTP = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_OTP);
        name = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_NAME);
        phone = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_PHONE);
        pass = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_PASS);
        profileUri = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_PROFILE);
        status = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_STATUS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);
        setInitialState();

        userAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_otp.setError(null);
                String sotp = otpView.getText().toString();

                Log.d("OTP",sotp+" "+OTP);
                if(sotp.isEmpty())
                {
                    reg_otp.setError("*This field is required.");
                }
                if(!sotp.equals(OTP)) {
                    reg_otp.setError("*Wrong OTP");
                }
                else {
                    dialog.show();
                    auth = FirebaseAuth.getInstance();
                    database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
                    storage = FirebaseStorage.getInstance();


                    Log.d("UserData",Email+pass+name+profileUri+status);
                    auth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                if(!profileUri.equals("None"))
                                {
                                    Log.d("UserData",Email+pass+name+profileUri+status);
                                    storageReference.putFile(Uri.parse(profileUri)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                Log.d("UserData",Email+pass+name+profileUri+status);
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        profileUri = uri.toString();
                                                        User user = new User(name,Email,phone,status,profileUri,auth.getUid());
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {

                                                                    dialog.dismiss();
                                                                    Toast.makeText(UserAuthActivity.this,"User Registered Successfully.",Toast.LENGTH_LONG).show();
                                                                    startActivity(new Intent(UserAuthActivity.this,LoginActivity.class));
                                                                    finish();

                                                                }else {
                                                                    dialog.dismiss();
                                                                    Toast.makeText(UserAuthActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();

                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                            else {
                                                dialog.dismiss();
                                                Toast.makeText(UserAuthActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                                else {
                                    profileUri = "https://firebasestorage.googleapis.com/v0/b/chattogether-19397.appspot.com/o/profile.png?alt=media&token=55170d34-4f1f-4161-9bcb-6b0d4f9e7ad5";
                                    User user = new User(name,Email,phone,status,profileUri,auth.getUid());
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                dialog.dismiss();
                                                Toast.makeText(UserAuthActivity.this,"User Registered Successfully.",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(UserAuthActivity.this,LoginActivity.class));
                                                finish();
                                            }
                                            else {
                                                dialog.dismiss();
                                                Toast.makeText(UserAuthActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(UserAuthActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}