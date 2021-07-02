package com.example.campusrecruitment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    Spinner roleView;
    EditText emailView;
    EditText passView;
    CheckBox remView;
    Button loginBtnView;

    String[] roles = new String[2];
    String role;
    String email;
    String pass;

    MainController controller;
    public static Context context;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setInitialState();



        roleView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                emailView.getText().clear();
                passView.getText().clear();
                emailView.setError(null);
                passView.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loginBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(validateFields())
                    {
                        //Toast.makeText(getBaseContext(),"Verified",Toast.LENGTH_SHORT).show();
                        boolean isVerified = controller.verifyUser(email,pass);
                        if(isVerified)
                        {
                            if(role.equals(roles[0])) {
                                Intent loginIn = new Intent(context,StudentHomePageActivity.class);
                                startActivity(loginIn);
                                finish();
                            }
                        }
                    }
            }
        });

    }

    public boolean validateFields()
    {
        role = roleView.getSelectedItem().toString();
        email = emailView.getText().toString();
        pass = passView.getText().toString();

        if(email.isEmpty())
        {
            emailView.setError("This field is required.");
            return false;
        }
        else if(email.indexOf('@')==-1)
        {
            emailView.setError("Please enter valid Email Address.");
            return false;
        }
        else {
            if(role.equals(roles[0]))
            {
                String getDomain = email.substring(email.indexOf('@')+1);
                if(!getDomain.equals("daiict.ac.in"))
                {
                    emailView.setError("You can only register by your DAIICT Domain Student-ID.");
                    return false;
                }
            }
            else if(role.equals(roles[1]))
            {
                String stMailValid = "^(.+)@(.+)$";
                if(!email.matches(stMailValid))
                {
                    emailView.setError("Please enter valid Email Address");
                    return false;
                }
            }
            else {

            }
        }

        String specialChars = "(.*[@,#,$,%].*$)";
        String numbers = "(.*[0-9].*)";
        String lowerCaseChars = "(.*[a-zA-Z].*)";
        if(pass.isEmpty())
        {
            passView.setError("This field is required.");
            return false;
        }
        else if(pass.length() > 16 || pass.length() < 8)
        {
            passView.setError("Password must be of 8 to 16 characters.");
            return false;
        }
        else if (!pass.matches(lowerCaseChars ))
        {
            passView.setError("Password must have at least one alphabet");
            return false;
        }
        else if (!pass.matches(numbers ))
        {
            passView.setError("Password must have atleast one number");
            return false;
        }
        else if (!pass.matches(specialChars ))
        {
            passView.setError("Password must have at least one special character among @#$%");
            return false;
        }
        return true;
    }

    public void setInitialState()
    {
        roleView = findViewById(R.id.Role);
        emailView = findViewById(R.id.userMail);
        passView = findViewById(R.id.userPass);
        remView = findViewById(R.id.rm);
        loginBtnView = findViewById(R.id.loginUserBtn);
        controller = MainActivity.controller;
        context = getBaseContext();
        roles = getResources().getStringArray(R.array.roleNames);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
    }
    public void regAct(View view)
    {
        Intent reg = new Intent(this, RegisterActivity.class);
        startActivity(reg);
        finish();
    }
    public void forgotPassAct(View view)
    {
        Intent fp = new Intent(this, ForgotPasswordActivity.class);
        startActivity(fp);
        finish();
    }
    public void showPass(View view)
    {
        ImageButton imgbtn= (ImageButton)findViewById(R.id.showLoginPassBtn);
        int tag = Integer.parseInt(imgbtn.getTag().toString());
        if(tag==0)
        {
            passView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.hide);
            imgbtn.setTag("1");
        }
        else
        {
            passView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.show);
            imgbtn.setTag("0");
        }
    }
}