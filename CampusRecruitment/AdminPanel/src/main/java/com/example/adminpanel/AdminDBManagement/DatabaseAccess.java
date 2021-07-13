package com.example.adminpanel.AdminDBManagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAccess {

    private static DatabaseAccess instance=null;
    private DBOpenHelper dbOpenHelper;

    private DatabaseAccess(Context context)
    {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context)
    {
        if(instance==null)
        {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }
    public void createInitialTables()
    {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL(adminDBParams.CREATE_QUERY);
        Log.d("create",adminDBParams.CREATE_QUERY);
        db.close();
    }
}
