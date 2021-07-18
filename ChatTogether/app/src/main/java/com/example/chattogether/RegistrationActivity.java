package com.example.chattogether;

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
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {


    TextInputLayout regEmail;
    TextInputLayout regPassword;
    TextInputLayout regName;
    TextInputLayout cPassword;

    TextInputEditText regEmailView;
    TextInputEditText regNameView;
    TextInputEditText regPassView;
    TextInputEditText CPassView;

    CircleImageView profileView;

    String Email;
    String Password;
    String CPassword;
    String Name;
    String profileUri;
    String status;

    Button registerBtn;

    private ProgressDialog dialog;
    BasicOperations operations;
    Handler mailHandler;

    public static String USER_MAIL_KEY="com.example.chattogether.RegistrationActivity.USER_MAIL";
    public static String USER_MAIL_OTP="com.example.chattogether.RegistrationActivity.USER_OTP";
    public static String USER_MAIL_PASS="com.example.chattogether.RegistrationActivity.USER_PASS";
    public static String USER_MAIL_NAME="com.example.chattogether.RegistrationActivity.USER_NAME";
    public static String USER_MAIL_PROFILE="com.example.chattogether.RegistrationActivity.USER_PROFILE";
    public static String USER_MAIL_STATUS="com.example.chattogether.RegistrationActivity.USER_STATUS";

    public int STORAGE_PERMISSION_CODE = 1;

    public void setInitialState()
    {
        regEmail = findViewById(R.id.reg_email);
        regName = findViewById(R.id.reg_name);
        regPassword = findViewById(R.id.reg_password);
        cPassword = findViewById(R.id.cpassword);

        regEmailView = findViewById(R.id.regEmailView);
        regPassView = findViewById(R.id.regPassView);
        regNameView = findViewById(R.id.regNameView);
        CPassView = findViewById(R.id.cpassView);
        profileView = findViewById(R.id.profile_image);

        registerBtn = findViewById(R.id.registerBtn);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait..");

        profileUri = "None";
        status = "Let's Chat Together!";
        operations = BasicOperations.getInstance();
        mailHandler = new Handler();

    }

    public void OTPAuthentication() {
        Random r = new Random();
        int otp = 1000 + r.nextInt(8999);
        operations.sendMail(Email, otp);
        Runnable check = new Runnable() {
            @Override
            public void run() {
                if (operations.getMailStatus() == ConditionCheckers.SEND_MAIL.DONE) {

                    dialog.dismiss();
                    mailHandler.removeCallbacks(this);
                    Toast.makeText(getBaseContext(), "OTP is sent to given Mail ID Successfully.", Toast.LENGTH_LONG).show();
                    Intent userAuth = new Intent(RegistrationActivity.this,UserAuthActivity.class);
                    userAuth.putExtra(USER_MAIL_OTP,String.valueOf(otp));
                    userAuth.putExtra(USER_MAIL_KEY,Email);
                    userAuth.putExtra(USER_MAIL_NAME,Name);
                    userAuth.putExtra(USER_MAIL_PASS,Password);
                    userAuth.putExtra(USER_MAIL_PROFILE,profileUri);
                    userAuth.putExtra(USER_MAIL_STATUS,status);
                    startActivity(userAuth);
                    finish();

                } else if (operations.getMailStatus() == ConditionCheckers.SEND_MAIL.ERROR) {
                    dialog.dismiss();
                    mailHandler.removeCallbacks(this);
                    Toast.makeText(getBaseContext(), "Something Went Wrong, check your network Connection and try again.", Toast.LENGTH_LONG).show();
                } else if (operations.getMailStatus() == ConditionCheckers.SEND_MAIL.PROGRESS) {
                    mailHandler.postDelayed(this, 1000);
                }
            }
        };
        mailHandler.postDelayed(check,0);
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
                            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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


    public void checkStoragePermission() {
        //check if permission is alreay granted to us or not.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(),"Opening Media",Toast.LENGTH_SHORT).show();
            pickProfile();
        } else {
            getStoragePermission();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setInitialState();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeErrors();
                boolean isValidated = validateForm();
                if(isValidated)
                {
                    dialog.show();
                    OTPAuthentication();
                }
            }
        });

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Title"),);*/
                checkStoragePermission();
            }
        });
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
                    profileView.setImageURI(uri);
                    profileUri = uri.toString();

                }
            });


    private void removeErrors() {
        regEmail.setError(null);
        regPassword.setError(null);
        cPassword.setError(null);
        regName.setError(null);
    }

    private boolean validateForm() {

        Email = regEmailView.getText().toString();
        Password = regPassView.getText().toString();
        CPassword = CPassView.getText().toString();
        Name = regNameView.getText().toString();

        if(Email.isEmpty())
        {
            regEmail.setError("*This field is required.");
            return false;
        }
        if(Email.indexOf('@')==-1)
        {
            regEmail.setError("*Please enter valid Email Address.");
            return false;
        }

        String numbers = "(.*[0-9].*)";
        String lowerCaseChars = "(.*[a-zA-Z ].*)";

        if (Name.isEmpty())
        {
            regName.setError("*This field is Required.");
            return false;
        }
        if(!Name.matches(lowerCaseChars))
        {
            regName.setError("*Enter Valid name.");
            return false;
        }
        if(Password.isEmpty())
        {
            regPassword.setError("*This field is required.");
            return false;
        }
        else if(Password.length() > 16 || Password.length() < 8)
        {
            regPassword.setError("*Password must be of 8 to 16 characters.");
            return false;
        }
        else if (!Password.matches(lowerCaseChars ))
        {
            regPassword.setError("*Password must have at least one alphabet");
            return false;
        }
        else if (!Password.matches(numbers ))
        {
            regPassword.setError("*Password must have at least one number");
            return false;
        }
        if(!Password.equals(CPassword))
        {
            cPassword.setError("*Password And Confirm Password does not match.");
            return false;
        }
        return true;
    }

    public void redirectLogin(View view)
    {
        startActivity(new Intent(getBaseContext(),LoginActivity.class));
        finish();
    }
}