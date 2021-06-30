package com.example.campusrecruitment.BasicOperations;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.campusrecruitment.Dialogs.OTPDialog;
import com.example.campusrecruitment.MainController;
import com.example.campusrecruitment.StudentRegisterActivity;
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

        boolean isDone=false;
        try {

            isDone = (boolean)isSent.get(10, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            Log.d("mailERR",e.toString());
            Toast.makeText(StudentRegisterActivity.context,"Something went wrong, try again.",Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Log.d("mailERR",e.toString());
            Toast.makeText(StudentRegisterActivity.context,"Something went wrong, try again.",Toast.LENGTH_LONG).show();
        }catch (TimeoutException e)
        {
            Toast.makeText(StudentRegisterActivity.context,"It is taking Longer than Expected, Please check your Internet Connection and Try Again.",Toast.LENGTH_LONG).show();
        }

        if(isDone)
        {
            Toast.makeText(StudentRegisterActivity.context,"OTP is sent Successfully to given mailID.",Toast.LENGTH_LONG).show();
        }

        CompletableFuture.runAsync(()->{
            OTPDialog otpDialog = new OTPDialog(OTP,category);
            otpDialog.show(StudentRegisterActivity.fragmentManager,null);
        });
    }
}
