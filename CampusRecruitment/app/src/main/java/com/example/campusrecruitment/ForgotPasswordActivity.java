package com.example.campusrecruitment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.campusrecruitment.BasicOperations.ChangePasswordHandler;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mailView;
    private Button btnView;
    private Spinner roleView;

    private String regMail;
    private String role;
    private String[] roles = new String[2];

    private MainController mainController;

    public static Context context;
    public static ChangePasswordHandler handler;
    public static boolean taskCheck = false;
    public ProgressBar loadView;

    public static final String MAIL_KEY = "com.example.campusrecruitment.ForgotPasswordActivity.EMAIL";
    public static final String CAT = "com.example.campusrecruitment.ForgotPasswordActivity.CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setInitialState();

        roleView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mailView.getText().clear();
                mailView.setError(null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                boolean isValidated = validateField();
                if(isValidated)
                {
                    //Toast.makeText(getBaseContext(),"verified",Toast.LENGTH_LONG).show();
                    if(role.equals(roles[0]))
                    {
                        if(mainController.checkIfStudentExists(regMail))
                        {
                            //Toast.makeText(getBaseContext(),"Registered.",Toast.LENGTH_LONG).show();
                            handler = new ChangePasswordHandler();
                            handler.sendEmailForChangePassword(regMail);
                            loadView.setVisibility(View.VISIBLE);
                            Handler checkHandler = new Handler();
                            Runnable checkTask = new Runnable() {
                                @Override
                                public void run() {
                                    if(taskCheck)
                                    {
                                        Intent activity = new Intent(getBaseContext(),ChangePasswordActivity.class);
                                        activity.putExtra(MAIL_KEY,regMail);
                                        activity.putExtra(CAT,roles[0]);
                                        startActivity(activity);
                                        loadView.setVisibility(View.INVISIBLE);
                                        checkHandler.removeCallbacks(this);
                                        finish();
                                    }
                                    else {
                                        checkHandler.postDelayed(this,1000);
                                    }
                                }
                            };
                            checkHandler.postDelayed(checkTask,1000);
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Student with this ID does not Exist.",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("inCall","Stop");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("inCall","onPause");
    }

    public boolean validateField()
    {
        role = roleView.getSelectedItem().toString();
        regMail = mailView.getText().toString();

        if(regMail.isEmpty())
        {
            mailView.setError("This field is required.");
            return false;
        }
        else if(regMail.indexOf('@')==-1)
        {
            mailView.setError("Please enter valid Email Address.");
            return false;
        }
        else {
            if(role.equals(roles[0]))
            {
                String getDomain = regMail.substring(regMail.indexOf('@')+1);
                if(!getDomain.equals("daiict.ac.in"))
                {
                    mailView.setError("You can only enter your DAIICT Domain Student-ID.");
                    return false;
                }
            }
            if(role.equals(roles[1]))
            {
                String stMailValid = "^(.+)@(.+)$";
                if(!regMail.matches(stMailValid))
                {
                    mailView.setError("Please enter valid Email Address");
                    return false;
                }
            }
        }
        return true;
    }
    public void setInitialState()
    {
     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
        mailView = findViewById(R.id.regMail);
        btnView = findViewById(R.id.regMailBtn);
        roleView = findViewById(R.id.fgRole);
        roles = getResources().getStringArray(R.array.roleNames);
        loadView = findViewById(R.id.loading);
        context = getBaseContext();
        mainController = MainActivity.controller;
    }
}