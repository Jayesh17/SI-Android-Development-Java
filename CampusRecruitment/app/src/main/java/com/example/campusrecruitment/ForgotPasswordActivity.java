package com.example.campusrecruitment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    public static Context context;
    private MainController mainController;



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
                            ChangePasswordHandler handler = new ChangePasswordHandler();
                            handler.sendEmailForChangePassword(regMail);
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
        context = getBaseContext();
        mainController = MainActivity.controller;
    }
}