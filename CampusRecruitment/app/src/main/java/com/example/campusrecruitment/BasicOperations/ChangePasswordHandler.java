package com.example.campusrecruitment.BasicOperations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.campusrecruitment.ChangePasswordActivity;
import com.example.campusrecruitment.DBManipulation.DBHandler;
import com.example.campusrecruitment.ForgotPasswordActivity;
import com.example.campusrecruitment.LoginActivity;
import com.example.campusrecruitment.MainActivity;
import com.example.campusrecruitment.MainController;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ChangePasswordHandler {

    MailManagement mailManagement;
    DBHandler dbHandler;
    int OTP;

    public ChangePasswordHandler()
    {
        mailManagement = MainController.getMailManagement();
        dbHandler = MainController.getDbHandler();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendEmailForChangePassword(final String email) {
        Random r = new Random();
        OTP = 1000 + (r.nextInt(8999));

        CompletableFuture<Boolean> task = CompletableFuture.supplyAsync(() -> {
            Log.d("inCall", "in Call");
            return mailManagement.sendOTPForChangePassword(email, OTP);
        });


        Handler handler = new Handler();

        final boolean[] isDone = {false};

        Runnable checkTask = new Runnable() {
            @Override
            public void run() {
               if(task.isDone())
               {
                   try {
                       isDone[0] = (boolean) task.get(10, TimeUnit.SECONDS);
                       if (isDone[0]) {
                           Log.d("inCall", isDone[0] + "");
                           Toast.makeText(ForgotPasswordActivity.context, "OTP is sent Successfully to given mailID.", Toast.LENGTH_LONG).show();
                           ForgotPasswordActivity.taskCheck = isDone[0];
                       }
                   } catch (ExecutionException | InterruptedException e) {
                       Log.d("mailERR", e.toString());
                       Toast.makeText(ForgotPasswordActivity.context, "Something went wrong, try again.", Toast.LENGTH_LONG).show();
                   } catch (TimeoutException e) {
                       Toast.makeText(ForgotPasswordActivity.context, "taking Longer than expected, Please try again.", Toast.LENGTH_LONG).show();
                   }
                   finally {
                       handler.removeCallbacks(this);
                   }
               }
               else {
                   handler.postDelayed(this,1000);
               }
            }
        };
        handler.postDelayed(checkTask,1000);
    }

    public boolean checkOTP(int otp) {
        return OTP==otp;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changePasswordForStudent(final String email, final String pass) {

        CompletableFuture<Boolean> task = CompletableFuture.supplyAsync(()->{
            return dbHandler.changePasswordOfStudent(email,pass);
        });

        boolean isDone = false;
        try {
            isDone = (boolean)task.get(5, TimeUnit.SECONDS);
            if(isDone)
            {

                Log.d("success","update s");
                Toast.makeText(ChangePasswordActivity.context,"Password Changed Successfully.",Toast.LENGTH_LONG).show();
            }
        }
        catch (ExecutionException |InterruptedException e) {

            e.printStackTrace();
            Log.d("fail","update f");
            Toast.makeText(ChangePasswordActivity.context,"Something went Wrong, Try again later.",Toast.LENGTH_LONG).show();
        }
        catch (TimeoutException e) {

            e.printStackTrace();
            Log.d("fail","update f");
            Toast.makeText(ChangePasswordActivity.context,"Taking longer then Expected, Try again later.",Toast.LENGTH_LONG).show();
        }
    }
}
