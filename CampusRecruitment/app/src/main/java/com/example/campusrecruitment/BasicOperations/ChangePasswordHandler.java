package com.example.campusrecruitment.BasicOperations;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.campusrecruitment.ForgotPasswordActivity;
import com.example.campusrecruitment.MainController;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ChangePasswordHandler {

    MailManagement mailManagement;

    public ChangePasswordHandler()
    {
        mailManagement = MainController.getMailManagement();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendEmailForChangePassword(final String email)
    {
        Random r = new Random();
        int OTP = 1000+(r.nextInt(8999));
        Runnable sendMail = new Runnable() {
            @Override
            public void run() {
                Log.d("inCall","in Call");
                boolean isDone = mailManagement.sendOTPForChangePassword(email,OTP);
                if(isDone)
                {
                    Log.d("inCall",isDone+"l");
                    Toast.makeText(ForgotPasswordActivity.context,"OTP is sent Successfully to given mailID.",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.context,"Something went wrong, try again.",Toast.LENGTH_LONG).show();
                }
            }
        };

        CompletableFuture.runAsync(sendMail);
    }
}
