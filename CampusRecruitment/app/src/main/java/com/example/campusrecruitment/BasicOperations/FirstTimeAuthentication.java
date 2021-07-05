package com.example.campusrecruitment.BasicOperations;

import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.campusrecruitment.Dialogs.OTPDialog;
import com.example.campusrecruitment.MainController;
import com.example.campusrecruitment.StudentRegisterActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FirstTimeAuthentication {

    MailManagement mailManagement;

    public FirstTimeAuthentication()
    {
        mailManagement = MainController.getMailManagement();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void OTPAuthentication(String recipient, Integer OTP,String category)
    {
        CompletableFuture<Boolean> isSent = CompletableFuture.supplyAsync(()->{
            return mailManagement.registerUserAuthentication(recipient,OTP);
        });

        final boolean[] isDone = {false};
        Handler checkHandler = new Handler();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if(isSent.isDone())
                {
                    try {

                        isDone[0] = (boolean)isSent.get(10, TimeUnit.SECONDS);
                        if(isDone[0])
                        {
                            StudentRegisterActivity.regLoading.setVisibility(View.INVISIBLE);
                            Toast.makeText(StudentRegisterActivity.context,"OTP is sent Successfully to given mailID.",Toast.LENGTH_LONG).show();

                            CompletableFuture.runAsync(()->{
                                OTPDialog otpDialog = new OTPDialog(OTP,category);
                                otpDialog.show(StudentRegisterActivity.fragmentManager,null);
                            });

                        }
                    } catch (ExecutionException e) {
                        Log.d("mailERR",e.toString());
                        StudentRegisterActivity.regLoading.setVisibility(View.INVISIBLE);
                        Toast.makeText(StudentRegisterActivity.context,"Something went wrong, try again.",Toast.LENGTH_LONG).show();

                    } catch (InterruptedException e) {
                        Log.d("mailERR",e.toString());
                        StudentRegisterActivity.regLoading.setVisibility(View.INVISIBLE);
                        Toast.makeText(StudentRegisterActivity.context,"Something went wrong, try again.",Toast.LENGTH_LONG).show();
                    }catch (TimeoutException e)
                    {
                        StudentRegisterActivity.regLoading.setVisibility(View.INVISIBLE);
                        Toast.makeText(StudentRegisterActivity.context,"It is taking Longer than Expected, Please check your Internet Connection and Try Again.",Toast.LENGTH_LONG).show();
                    }
                    checkHandler.removeCallbacks(this);
                }
                else {
                    checkHandler.postDelayed(this,500);
                }
            }
        };

        checkHandler.postDelayed(task,500);
    }
}
