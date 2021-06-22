package com.example.campusrecruitment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
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
                boolean isSend = false;
                if(isValidated)
                {
                    isSend = OTPAuthetication();
                    if(isSend)
                    {
                        Toast.makeText(getBaseContext(),"checked!",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //Toast.makeText(getBaseContext(),"Failed!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean OTPAuthetication()
    {
        Random r = new Random();
        int OTP = 1000 + r.nextInt(8999);
        return MainActivity.mailBGTasks.OTPAuthentication(email,OTP);
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
            studPassView.setError("Password must be of 8 to 20 characters.");
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
    }

    public void showPass(View view)
    {
        int tag = Integer.parseInt(view.getTag().toString());
        ImageButton imgbtn= (ImageButton)findViewById(R.id.showPassBtn);
        if(tag==0)
        {
            studPassView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.hide);
            imgbtn.setTag("1");
        }
        else
        {
            studPassView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.show);
            imgbtn.setTag("0");
        }
    }
    public void showCPass(View view)
    {
        int tag = Integer.parseInt(view.getTag().toString());
        ImageButton imgbtn= (ImageButton)findViewById(R.id.showCPassBtn);
        if(tag==0)
        {
            studCPassView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.hide);
            view.setTag("1");
        }
        else
        {
            studCPassView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgbtn.setImageResource(R.drawable.show);
            view.setTag("0");
        }
    }
}