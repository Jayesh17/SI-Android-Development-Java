package com.example.campusrecruitment.DBManipulation;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBOpenHelper extends SQLiteAssetHelper {
    public static final String DB_NAME = "CampusRecruitmentDB.db";
    public static final int DB_VERSION = 1; //everytime we update database we change the version

    public DBOpenHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }
}
