package com.example.chattogether;

import android.util.Log;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailManagement {

    private static final String userName= "chattogether1607@gmail.com";

    private static final String password= "ChatTogether8781";

    final String OTP_AUTH_SUBJECT="Welcome to Chat Together!";
    final String OTP_AUTH_MSG="Your One-Time Password for Registration is:";

    final String CP_REQUEST_SUBJECT="Change Password Confirmation";
    final String CP_REQUEST_MSG="Your One-Time Password to change your password is:";

    final String REGARD= "Thanks And Regards";
    final String COM_NAME="Chat Together!";

    private Properties prop;
    private Session session;

    private static MailManagement mailManagement=null;

    private MailManagement()
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

    public static MailManagement getInstance()
    {
        if(mailManagement==null)
        {
            mailManagement = new MailManagement();
        }
        return mailManagement;
    }

    public ConditionCheckers.SEND_MAIL registerUserAuthentication(String recipient,Integer OTP)
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
            return ConditionCheckers.SEND_MAIL.ERROR;
        }
        return ConditionCheckers.SEND_MAIL.DONE;
    }

}
