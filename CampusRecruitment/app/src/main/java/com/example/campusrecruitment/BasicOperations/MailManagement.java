package com.example.campusrecruitment.BasicOperations;

import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailManagement {
    public static final String userName= "recruiteasy01@gmail.com";

    public static final String password= "RecruitEasy@01";

    final String OTP_AUTH_SUBJECT="Welcome to RecruitEasy!";
    final String OTP_AUTH_MSG="Your One-Time Password for Registration is:";

    final String CP_REQUEST_SUBJECT="Change Password Confirmation";
    final String CP_REQUEST_MSG="Your One-Time Password to change your password is:";

    final String REGARD= "Thanks And Regards";
    final String COM_NAME="RecruitEasy!";

    private Properties prop;
    private Session session;
    public MailManagement()
    {
        prop = new Properties();
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable","true");
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","587");

        session = Session.getInstance(prop,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName,password);
            }
        });

    }
    public boolean registerUserAuthentication(String recipient,Integer OTP)
    {
        try
        {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(userName));
            msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipient));
            msg.setSubject(OTP_AUTH_SUBJECT);
            msg.setText(OTP_AUTH_MSG+" "+OTP+"\r\n"+REGARD+"\r\n"+COM_NAME);
            Transport.send(msg);
        }
        catch (MessagingException e)
        {
            Log.d("mailERR:",e.toString());
            return false;
        }
        return true;
    }

    public boolean sendOTPForChangePassword(final String recipient,final int OTP) {

        try
        {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(userName));
            msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipient));
            msg.setSubject(CP_REQUEST_SUBJECT);
            msg.setText(CP_REQUEST_MSG+" "+OTP+"\r\n"+REGARD+"\r\n"+COM_NAME);
            Transport.send(msg);
            Log.d("inCall","in Call mail");
        }
        catch (Exception e)
        {
            Log.d("mailERR:",e.toString());
            return false;
        }
        return true;
    }
}
