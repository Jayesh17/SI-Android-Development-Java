package com.example.campusrecruitment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campusrecruitment.BasicOperations.FirstTimeAuthentication;
import com.example.campusrecruitment.Dialogs.OTPDialog;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentRegisterActivity extends AppCompatActivity {

    private EditText studEmailView;
    private EditText studNameView;
    private EditText studPassView;
    private EditText studCPassView;
    private Button submitView;
    public static Context context;
    public static FragmentManager fragmentManager;
    String email;
    String name ;
    String pass;
    String cpass;
    Handler checkHandler;

    MainController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        setInitialState();

        submitView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Boolean isValidated = validateForm();
                if(isValidated)
                {
                    Toast.makeText(StudentRegisterActivity.context,"Please wait...",Toast.LENGTH_LONG).show();
                    if(controller.checkIfStudentExists(email))
                    {
                        Toast.makeText(StudentRegisterActivity.context,"Student with Mail ID "+email+" Already Registered.",Toast.LENGTH_LONG).show();
                    }
                    else {
                        OTPAuthentication();
                        checkHandler.postDelayed(isVerified,1000);
                    }
                }
            }
        });
    }

    private void openLogin()
    {
        Intent loginPage = new Intent(getBaseContext(),LoginActivity.class);
        startActivity(loginPage);
        finish();
    }
    Runnable isVerified = new Runnable() {
        @Override
        public void run() {
            if(OTPDialog.isVerified==1)
            {
                Log.d("callbacks","Verified");
                Toast.makeText(StudentRegisterActivity.context,"Student Verified",Toast.LENGTH_LONG).show();
                 checkHandler.removeCallbacks(this);
                 controller.registerStudent(email,name,pass);
                 openLogin();
            }
            else if(OTPDialog.isVerified==0){
                Log.d("callbacks","Canceled");
                Toast.makeText(StudentRegisterActivity.context,"Operation Canceled.",Toast.LENGTH_LONG).show();
                checkHandler.removeCallbacks(this);
            }
            else {
                Log.d("callbacks","in Waiting");
                checkHandler.postDelayed(this,1000);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkHandler.removeCallbacks(isVerified);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void OTPAuthentication()
    {
        Random r = new Random();
        int OTP = 1000 + r.nextInt(8999);
        FirstTimeAuthentication auth = new FirstTimeAuthentication();
        auth.OTPAuthentication(email,OTP,"Student");
    }
    private Boolean validateForm()
    {
        email = studEmailView.getText().toString();
        name = studNameView.getText().toString();
        pass = studPassView.getText().toString();
        cpass = studCPassView.getText().toString();

        if(email.isEmpty())
        {
            studEmailView.setError("This field is required!");
            return false;
        }
        if(email.indexOf('@')==-1)
        {
            studEmailView.setError("Please enter valid Email-ID.");
            return false;
        }
        String getDomain = email.substring(email.indexOf('@')+1);
        Log.d("emailv",getDomain);
        if(!getDomain.equals("daiict.ac.in"))
        {
            studEmailView.setError("You can only register by your DAIICT Domain Student-ID.");
            return false;
        }

        if(name.isEmpty())
        {
            studNameView.setError("This field is required!");
            return false;
        }
        String specialChars = "(.*[@,#,$,%].*$)";
        String numbers = "(.*[0-9].*)";
        if(name.matches(specialChars) || name.matches(numbers))
        {
            studNameView.setError("Please enter a valid Name.");
            return false;
        }

        if(pass.isEmpty())
        {
            studPassView.setError("This field is required!");
            return false;
        }
        if(pass.length() > 16 || pass.length() < 8)
        {
            studPassView.setError("Password must be of 8 to 16 characters.");
            return false;
        }
        String lowerCaseChars = "(.*[a-zA-Z].*)";
        if (!pass.matches(lowerCaseChars ))
        {
            studPassView.setError("Password must have atleast one alphabet");
            return false;
        }

        if (!pass.matches(numbers ))
        {
            studPassView.setError("Password must have atleast one number");
            return false;
        }
        if (!pass.matches(specialChars ))
        {
            studPassView.setError("Password must have atleast one special character among @#$%");
            return false;
        }

        if(cpass.isEmpty())
        {
            studCPassView.setError("This field is required!");
            return false;
        }
        if(!pass.equals(cpass))
        {
            studCPassView.setError("Password And Confirm Password does not match.");
            return false;
        }
        return true;
    }
    private void setInitialState()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
        studEmailView = (EditText)findViewById(R.id.studMail);
        studNameView = (EditText)findViewById(R.id.studName);
        studPassView = (EditText)findViewById(R.id.studPass);
        studCPassView = (EditText) findViewById(R.id.studCPass);
        submitView = (Button)findViewById(R.id.studRegisterBtn);
        context = getBaseContext();
        fragmentManager = getSupportFragmentManager();
        checkHandler = new Handler();
        controller = MainActivity.controller;
    }

    public void showPass(View view)
    {
        ImageButton imgbtn= (ImageButton)findViewById(R.id.showPassBtn);
        int tag = Integer.parseInt(imgbtn.getTag().toString());
        if(tag==0)
        {
            studPassView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.hide);
            imgbtn.setTag("1");
        }
        else
        {
            studPassView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.show);
            imgbtn.setTag("0");
        }
    }
    public void showCPass(View view)
    {
        ImageButton imgbtn= (ImageButton)findViewById(R.id.showCPassBtn);
        int tag = Integer.parseInt(imgbtn.getTag().toString());
        if(tag==0)
        {
            studCPassView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.hide);
            imgbtn.setTag("1");
        }
        else
        {
            studCPassView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.show);
            imgbtn.setTag("0");
        }
    }
}