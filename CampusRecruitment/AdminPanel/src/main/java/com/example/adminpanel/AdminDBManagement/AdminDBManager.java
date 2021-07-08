package com.example.adminpanel.AdminDBManagement;

import android.database.sqlite.SQLiteDatabase;

import com.example.campusrecruitment.DBManipulation.DBHandler;
import com.example.campusrecruitment.MainController;

public class AdminDBManager {

    static SQLiteDatabase readableDB;
    static SQLiteDatabase writableDB;

    private AdminDBManager(){
        readableDB = null;
        writableDB = null;
    }
    public static boolean mergeDB()
    {
        if(writableDB == null && readableDB==null)
        {
            DBHandler handler = MainController.getDbHandler();
            writableDB = handler.getWritableDB();
            readableDB = handler.getReadableDB();
            return true;
        }
        return false;
    }
}
