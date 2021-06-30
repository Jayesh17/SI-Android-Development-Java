package com.example.campusrecruitment;

import com.example.campusrecruitment.BackGroundTasks.InitBGTask;
import com.example.campusrecruitment.BasicOperations.MailManagement;
import com.example.campusrecruitment.DBManipulation.DBHandler;

public class MainController {

    public static DBHandler dbHandler;
    public static MailManagement mailManagement;
    InitBGTask createDB;

    public MainController()
    {
        createDB = new InitBGTask();
        createDB.execute();
        mailManagement = new MailManagement();
    }

    public static MailManagement getMailManagement() {
        return mailManagement;
    }
}
