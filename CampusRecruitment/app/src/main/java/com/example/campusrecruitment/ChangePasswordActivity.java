package com.example.campusrecruitment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.campusrecruitment.BasicOperations.ChangePasswordHandler;
import com.example.campusrecruitment.Dialogs.OTPDialog;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText OTPView;
    EditText passView;
    EditText cpassView;
    Button submitView;

    String email;
    String category;
    String[] roles = new String[2];
    int OTP;
    String pass;
    String cpass;
    ChangePasswordHandler handler;
    public static Context context;

    public void setInitialState()
    {
        OTPView = findViewById(R.id.OTPField);
        passView = findViewById(R.id.CPPassField);
        cpassView = findViewById(R.id.CPCPassField);
        submitView = findViewById(R.id.CPBtn);

        Intent prevIntent = getIntent();
        email = prevIntent.getStringExtra(ForgotPasswordActivity.MAIL_KEY);
        category = prevIntent.getStringExtra(ForgotPasswordActivity.CAT);
        roles = getResources().getStringArray(R.array.roleNames);
        handler = ForgotPasswordActivity.handler;
        context = getBaseContext();
    }

    public boolean validateFields()
    {
        String strOTP = OTPView.getText().toString();
        pass = passView.getText().toString();
        cpass = cpassView.getText().toString();

        if(strOTP.isEmpty())
        {
            OTPView.setError("Please Enter the OTP sent to your mail address.");
            return false;
        }


        if(pass.isEmpty())
        {
            passView.setError("This field is required!");
            return false;
        }
        if(pass.length() > 16 || pass.length() < 8)
        {
            passView.setError("Password must be of 8 to 16 characters.");
            return false;
        }

        String specialChars = "(.*[@,#,$,%].*$)";
        String numbers = "(.*[0-9].*)";
        String lowerCaseChars = "(.*[a-zA-Z].*)";
        if (!pass.matches(lowerCaseChars ))
        {
            passView.setError("Password must have atleast one alphabet");
            return false;
        }

        if (!pass.matches(numbers ))
        {
            passView.setError("Password must have atleast one number");
            return false;
        }
        if (!pass.matches(specialChars ))
        {
            passView.setError("Password must have atleast one special character among @#$%");
            return false;
        }

        if(cpass.isEmpty())
        {
            cpassView.setError("This field is required!");
            return false;
        }
        if(!pass.equals(cpass))
        {
            cpassView.setError("Password And Confirm Password does not match.");
            return false;
        }

        if(strOTP.length()!=4)
        {
            OTPView.setError("Please Enter the Valid 4 digit OTP.");
            return false;
        }
        OTP = Integer.parseInt(strOTP);
        if(!handler.checkOTP(OTP))
        {
            OTPView.setError("Wrong OTP.");
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setInitialState();

        submitView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(validateFields())
                {
                    //Toast.makeText(getBaseContext(),"verified",Toast.LENGTH_SHORT).show();
                    if(category.equals(roles[0]))
                    {
                        handler.changePasswordForStudent(email,pass);
                        Intent activity = new Intent(getBaseContext(),LoginActivity.class);
                        startActivity(activity);
                        finish();
                    }
                }
            }
        });
    }
}