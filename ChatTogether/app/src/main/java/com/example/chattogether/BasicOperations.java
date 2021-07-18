package com.example.chattogether;
import android.os.CountDownTimer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicOperations {

    MailManagement mailManagement;
    private static BasicOperations operations=null;
    ConditionCheckers.SEND_MAIL mailStatus;

    private BasicOperations()
    {
        mailManagement = MailManagement.getInstance();
        mailStatus = ConditionCheckers.SEND_MAIL.PROGRESS;
    }

    public ConditionCheckers.SEND_MAIL getMailStatus() {
        return mailStatus;
    }

    public static BasicOperations getInstance()
    {
        if(operations==null)
        {
            operations = new BasicOperations();
        }
        return operations;
    }

    public void sendMail(String Email,int otp)
    {
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(new Runnable() {
            @Override
            public void run() {
                mailStatus = mailManagement.registerUserAuthentication(Email,otp);
            }
        });
    }
}
