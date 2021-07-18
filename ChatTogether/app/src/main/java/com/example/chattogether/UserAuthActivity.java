package com.example.chattogether;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UserAuthActivity extends AppCompatActivity {

    TextInputLayout reg_otp;
    TextInputEditText otpView;
    Button userAuthBtn;
    ProgressDialog dialog;

    String OTP;
    String Email;
    String name;
    String pass;
    String status;
    String profileUri;

    FireBaseManagement fireBaseManagement;

    public static Context context;
    public static ConditionCheckers.REGISTER_USER userStatus;

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
        pass = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_PASS);
        profileUri = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_PROFILE);
        status = getIntent.getStringExtra(RegistrationActivity.USER_MAIL_STATUS);

        fireBaseManagement = FireBaseManagement.getInstance();
        context=getBaseContext();
        userStatus = ConditionCheckers.REGISTER_USER.PROGRESS;
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
                    fireBaseManagement.createUser(Email,pass,name,profileUri,status);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(UserAuthActivity.userStatus== ConditionCheckers.REGISTER_USER.DONE)
                            {
                                dialog.dismiss();
                                Toast.makeText(getBaseContext(),"User registered Successfully.",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(context,LoginActivity.class));
                                handler.removeCallbacks(this);
                                finish();
                            }
                            else if(UserAuthActivity.userStatus== ConditionCheckers.REGISTER_USER.ERROR)
                            {
                                dialog.dismiss();
                                handler.removeCallbacks(this);
                            }
                            else {
                                handler.postDelayed(this,1000);
                            }

                        }
                    },0);
                }
            }
        });
    }
}