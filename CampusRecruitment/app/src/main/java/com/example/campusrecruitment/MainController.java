package com.example.campusrecruitment;

import android.util.Log;
import android.widget.Toast;

import com.example.campusrecruitment.BackGroundTasks.InitBGTask;
import com.example.campusrecruitment.BasicOperations.MailManagement;
import com.example.campusrecruitment.DBManipulation.DatabaseAccess;
import com.example.campusrecruitment.Params.StudentRegistrationParams;

public class MainController {

    public static MailManagement mailManagement;
    InitBGTask createDB;
    DatabaseAccess databaseAccess;

    public MainController()
    {
        mailManagement = new MailManagement();
        databaseAccess = DatabaseAccess.getInstance(MainActivity.context);
        createDB = new InitBGTask();
        createDB.execute(databaseAccess);
    }

    public static MailManagement getMailManagement() {
        return mailManagement;
    }

    public void registerStudent(final String email,final String name,final String pass) {

       //Handler tempHandler = new Handler();

       Runnable saveRecords = new Runnable() {
           @Override
           public void run() {

                if(databaseAccess.saveStudentInformation(email,name,pass))
                {
                    Log.d("RegSuccess","Student inserted.");
                    Toast.makeText(StudentRegisterActivity.context,"Student Registered Successfully.",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(StudentRegisterActivity.context,"Something Went Wrong.",Toast.LENGTH_LONG).show();
                }
               }
       };

       saveRecords.run();
    }

    public boolean checkIfStudentExists(final String email)
    {
        return databaseAccess.checkIfStudentExists(email);

    }

    public boolean verifyUser(final String email, final String pass) {

        StudentRegistrationParams.status currStatus;
        currStatus= databaseAccess.checkUserCredentials(email,pass);

        if(currStatus == StudentRegistrationParams.status.ERROR)
        {
            Toast.makeText(LoginActivity.context,"Something went Wrong, Try again later.",Toast.LENGTH_LONG).show();
            return false;
        }
        if(currStatus == StudentRegistrationParams.status.NOT_EXIST)
        {
            Toast.makeText(LoginActivity.context,"User Does not Exist.",Toast.LENGTH_LONG).show();
            return false;
        }
        if(currStatus == StudentRegistrationParams.status.WRONG_PASS)
        {
            Toast.makeText(LoginActivity.context,"Password is incorrect.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}
