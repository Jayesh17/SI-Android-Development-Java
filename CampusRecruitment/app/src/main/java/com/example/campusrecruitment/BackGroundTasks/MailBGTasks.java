package com.example.campusrecruitment.BackGroundTasks;

import android.Manifest;
import android.os.Build;
import android.os.CpuUsageInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.campusrecruitment.Dialogs.OTPDialog;
import com.example.campusrecruitment.StudentRegisterActivity;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MailBGTasks {

    public static MailManagement mailManagement;

    public MailBGTasks()
    {
        mailManagement = new MailManagement();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean OTPAuthentication(String recipient,Integer OTP)
    {
        CompletableFuture<Boolean> isSent = CompletableFuture.supplyAsync(()->{
            return mailManagement.registerUserAuthentication(recipient,OTP);
        });

        boolean isDone=false;
        try {
            isDone = (boolean)isSent.get();
        } catch (ExecutionException e) {
            Log.d("mailERR",e.toString());
        } catch (InterruptedException e) {
            Log.d("mailERR",e.toString());
        }

        if(isDone)
        {
            Toast.makeText(StudentRegisterActivity.context,"OTP is sent Successfully to given mailID.",Toast.LENGTH_LONG).show();
        }
        else {
            return false;
        }

        OTPDialog otpDialog = new OTPDialog();
        otpDialog.show(StudentRegisterActivity.fragmentManager,null);

        try {
            wait();
            Toast.makeText(StudentRegisterActivity.context,"finished",Toast.LENGTH_LONG).show();
            if(OTP==OTPDialog.otpReceived)
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
